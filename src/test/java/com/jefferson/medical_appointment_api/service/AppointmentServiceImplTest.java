package com.jefferson.medical_appointment_api.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jefferson.medical_appointment_api.dto.request.AppointmentRequest;
import com.jefferson.medical_appointment_api.dto.request.RescheduleAppointmentRequest;
import com.jefferson.medical_appointment_api.dto.response.AppointmentResponse;
import com.jefferson.medical_appointment_api.entity.AppointmentEntity;
import com.jefferson.medical_appointment_api.entity.MedicalDoctorEntity;
import com.jefferson.medical_appointment_api.entity.PatientEntity;
import com.jefferson.medical_appointment_api.enums.AppointmentStatus;
import com.jefferson.medical_appointment_api.exception.ResourceNotFoundException;
import com.jefferson.medical_appointment_api.mapper.AppointmentMapper;
import com.jefferson.medical_appointment_api.repository.AppointmentRepository;
import com.jefferson.medical_appointment_api.repository.MedicalDoctorRepository;
import com.jefferson.medical_appointment_api.repository.PatientRepository;
import com.jefferson.medical_appointment_api.repository.PenaltyRepository;
import com.jefferson.medical_appointment_api.service.impl.AppointmentServiceImpl;
import com.jefferson.medical_appointment_api.validation.AppointmentAvailability;
import com.jefferson.medical_appointment_api.validation.AppointmentValidator;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

  @Mock
  private AppointmentRepository appointmentRepository;

  @Mock
  private MedicalDoctorRepository medicalDoctorRepository;

  @Mock
  private PatientRepository patientRepository;

  @Mock
  private PenaltyRepository penaltyRepository;

  @Mock
  private AppointmentMapper appointmentMapper;

  @Mock
  private AppointmentValidator appointmentValidator;

  @Mock
  private AppointmentAvailability appointmentAvailability;

  @InjectMocks
  private AppointmentServiceImpl service;

  @Test
  void createAppointment_shouldCreateAppointmentSuccessfully() {
    AppointmentRequest request = new AppointmentRequest(
        1L,
        1L,
        LocalDateTime.of(2026, 7, 6, 10, 0)
    );

    MedicalDoctorEntity doctor = MedicalDoctorEntity.builder()
        .id(1L)
        .fullName("Dra. María González")
        .build();

    PatientEntity patient = PatientEntity.builder()
        .id(1L)
        .fullName("Juan Pérez")
        .build();

    AppointmentEntity appointment = AppointmentEntity.builder()
        .id(1L)
        .doctor(doctor)
        .patient(patient)
        .appointmentDateTime(request.appointmentDateTime())
        .status(AppointmentStatus.PROGRAMMED)
        .build();

    AppointmentResponse response = new AppointmentResponse(
        1L,
        1L,
        "Juan Pérez",
        1L,
        "Dra. María González",
        request.appointmentDateTime(),
        AppointmentStatus.PROGRAMMED,
        null
    );

    when(medicalDoctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
    when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
    when(appointmentRepository.save(any(AppointmentEntity.class))).thenReturn(appointment);
    when(appointmentMapper.toResponse(appointment)).thenReturn(response);

    AppointmentResponse result = service.createAppointment(request);

    assertEquals(1L, result.id());
    assertEquals(AppointmentStatus.PROGRAMMED, result.status());

    verify(appointmentValidator).validateAppointmentDateTime(request.appointmentDateTime());
    verify(appointmentValidator).validatePatientPenalties(request.patientId());
    verify(appointmentValidator).validateDoctorAvailability(
        null,
        request.doctorId(),
        request.appointmentDateTime()
    );
    verify(appointmentValidator).validatePatientConflict(
        null,
        request.patientId(),
        request.doctorId(),
        request.appointmentDateTime()
    );
    verify(appointmentRepository).save(any(AppointmentEntity.class));
  }

  @Test
  void createAppointment_shouldThrowExceptionWhenDoctorDoesNotExist() {
    AppointmentRequest request = new AppointmentRequest(
        1L,
        99L,
        LocalDateTime.of(2026, 7, 6, 10, 0)
    );

    when(medicalDoctorRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> service.createAppointment(request)
    );
  }

  @Test
  void createAppointment_shouldThrowExceptionWhenPatientDoesNotExist() {
    AppointmentRequest request = new AppointmentRequest(
        99L,
        1L,
        LocalDateTime.of(2026, 7, 6, 10, 0)
    );

    when(medicalDoctorRepository.findById(1L))
        .thenReturn(Optional.of(MedicalDoctorEntity.builder().id(1L).build()));

    when(patientRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> service.createAppointment(request)
    );
  }

  @Test
  void cancelAppointment_shouldCancelAppointmentSuccessfully() {
    MedicalDoctorEntity doctor = MedicalDoctorEntity.builder()
        .id(1L)
        .fullName("Dra. María González")
        .build();

    PatientEntity patient = PatientEntity.builder()
        .id(1L)
        .fullName("Juan Pérez")
        .build();

    AppointmentEntity appointment = AppointmentEntity.builder()
        .id(1L)
        .doctor(doctor)
        .patient(patient)
        .appointmentDateTime(LocalDateTime.now().plusDays(5))
        .status(AppointmentStatus.PROGRAMMED)
        .build();

    AppointmentResponse response = new AppointmentResponse(
        1L,
        1L,
        "Juan Pérez",
        1L,
        "Dra. María González",
        appointment.getAppointmentDateTime(),
        AppointmentStatus.CANCELLED,
        LocalDateTime.now()
    );

    when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
    when(appointmentRepository.save(appointment)).thenReturn(appointment);
    when(appointmentMapper.toResponse(appointment)).thenReturn(response);

    AppointmentResponse result = service.cancelAppointment(1L);

    assertEquals(AppointmentStatus.CANCELLED, result.status());
    verify(appointmentRepository).save(appointment);
  }

  @Test
  void cancelAppointment_shouldThrowExceptionWhenAppointmentDoesNotExist() {
    when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> service.cancelAppointment(99L)
    );
  }

  @Test
  void rescheduleAppointment_shouldRescheduleAppointmentSuccessfully() {
    LocalDateTime oldDateTime = LocalDateTime.of(2026, 7, 6, 10, 0);
    LocalDateTime newDateTime = LocalDateTime.of(2026, 7, 6, 11, 0);

    RescheduleAppointmentRequest request = new RescheduleAppointmentRequest(newDateTime);

    MedicalDoctorEntity doctor = MedicalDoctorEntity.builder()
        .id(1L)
        .fullName("Dra. María González")
        .build();

    PatientEntity patient = PatientEntity.builder()
        .id(1L)
        .fullName("Juan Pérez")
        .build();

    AppointmentEntity appointment = AppointmentEntity.builder()
        .id(1L)
        .doctor(doctor)
        .patient(patient)
        .appointmentDateTime(oldDateTime)
        .status(AppointmentStatus.PROGRAMMED)
        .build();

    AppointmentResponse response = new AppointmentResponse(
        1L,
        1L,
        "Juan Pérez",
        1L,
        "Dra. María González",
        newDateTime,
        AppointmentStatus.PROGRAMMED,
        null
    );

    when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
    when(appointmentRepository.save(appointment)).thenReturn(appointment);
    when(appointmentMapper.toResponse(appointment)).thenReturn(response);

    AppointmentResponse result = service.rescheduleAppointment(1L, request);

    assertEquals(newDateTime, result.appointmentDateTime());

    verify(appointmentValidator).validateAppointmentDateTime(newDateTime);
    verify(appointmentValidator).validateDoctorAvailability(1L, 1L, newDateTime);
    verify(appointmentValidator).validatePatientConflict(1L, 1L, 1L, newDateTime);
    verify(appointmentRepository).save(appointment);
  }

  @Test
  void rescheduleAppointment_shouldThrowExceptionWhenAppointmentDoesNotExist() {
    RescheduleAppointmentRequest request = new RescheduleAppointmentRequest(
        LocalDateTime.of(2026, 7, 6, 11, 0)
    );

    when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> service.rescheduleAppointment(99L, request)
    );
  }
}