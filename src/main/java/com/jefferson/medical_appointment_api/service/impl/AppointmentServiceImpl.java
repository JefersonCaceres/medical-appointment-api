package com.jefferson.medical_appointment_api.service.impl;

import com.jefferson.medical_appointment_api.dto.request.AppointmentRequest;
import com.jefferson.medical_appointment_api.dto.response.AppointmentResponse;
import com.jefferson.medical_appointment_api.dto.response.AvailableSlotResponse;
import com.jefferson.medical_appointment_api.entity.AppointmentEntity;
import com.jefferson.medical_appointment_api.entity.MedicalDoctorEntity;
import com.jefferson.medical_appointment_api.entity.PatientEntity;
import com.jefferson.medical_appointment_api.entity.PenaltyEntity;
import com.jefferson.medical_appointment_api.enums.AppointmentStatus;
import com.jefferson.medical_appointment_api.exception.BusinessException;
import com.jefferson.medical_appointment_api.exception.ResourceNotFoundException;
import com.jefferson.medical_appointment_api.mapper.AppointmentMapper;
import com.jefferson.medical_appointment_api.repository.AppointmentRepository;
import com.jefferson.medical_appointment_api.repository.MedicalDoctorRepository;
import com.jefferson.medical_appointment_api.repository.PatientRepository;
import com.jefferson.medical_appointment_api.repository.PenaltyRepository;
import com.jefferson.medical_appointment_api.service.AppointmentService;
import com.jefferson.medical_appointment_api.validation.AppointmentAvailability;
import com.jefferson.medical_appointment_api.validation.AppointmentValidator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final MedicalDoctorRepository medicalDoctorRepository;
  private final PatientRepository patientRepository;
  private final AppointmentMapper appointmentMapper;
  private final AppointmentValidator appointmentValidator;
  private final AppointmentAvailability appointmentAvailability;
  private final PenaltyRepository penaltyRepository;

  @Override
  public AppointmentResponse createAppointment(AppointmentRequest request) {
    appointmentValidator.validateAppointmentDateTime(request.appointmentDateTime());

    MedicalDoctorEntity doctor = medicalDoctorRepository.findById(request.doctorId())
        .orElseThrow(() -> new ResourceNotFoundException("MedicalDoctor", "id", request.doctorId()));

    PatientEntity patient = patientRepository.findById(request.patientId())
        .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", request.patientId()));

    appointmentValidator.validateDoctorAvailability(
        request.doctorId(),
        request.appointmentDateTime()
    );

    appointmentValidator.validatePatientConflict(
        request.patientId(),
        request.doctorId(),
        request.appointmentDateTime()
    );

    AppointmentEntity appointment = AppointmentEntity.builder()
        .doctor(doctor)
        .patient(patient)
        .appointmentDateTime(request.appointmentDateTime())
        .status(AppointmentStatus.PROGRAMMED)
        .build();

    AppointmentEntity savedAppointment = appointmentRepository.save(appointment);

    return appointmentMapper.toResponse(savedAppointment);
  }
  @Override
  public List<AvailableSlotResponse> getAvailableSlots(
      Long doctorId,
      LocalDate startDate,
      LocalDate endDate) {

    medicalDoctorRepository.findById(doctorId)
        .orElseThrow(() -> new ResourceNotFoundException("MedicalDoctor", "id", doctorId));

    return appointmentAvailability.generateAvailableSlots(doctorId, startDate, endDate);
  }

  @Override
  public AppointmentResponse cancelAppointment(Long appointmentId) {
    AppointmentEntity appointment = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

    if (appointment.getStatus() != AppointmentStatus.PROGRAMMED) {
      throw new BusinessException("Only programmed appointments can be cancelled.");
    }

    LocalDateTime cancellationDateTime = LocalDateTime.now();

    appointment.setStatus(AppointmentStatus.CANCELLED);
    appointment.setCancellationDateTime(cancellationDateTime);

    if (isLateCancellation(appointment.getAppointmentDateTime(), cancellationDateTime)) {
      PenaltyEntity penalty = PenaltyEntity.builder()
          .patient(appointment.getPatient())
          .appointment(appointment)
          .penaltyDateTime(cancellationDateTime)
          .build();

      penaltyRepository.save(penalty);
    }

    AppointmentEntity savedAppointment = appointmentRepository.save(appointment);

    return appointmentMapper.toResponse(savedAppointment);
  }

  private boolean isLateCancellation(
      LocalDateTime appointmentDateTime,
      LocalDateTime cancellationDateTime) {

    return cancellationDateTime.isAfter(appointmentDateTime.minusHours(2));
  }
}
