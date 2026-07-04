package com.jefferson.medical_appointment_api.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.jefferson.medical_appointment_api.enums.AppointmentStatus;
import com.jefferson.medical_appointment_api.exception.BusinessException;
import com.jefferson.medical_appointment_api.repository.AppointmentRepository;
import com.jefferson.medical_appointment_api.repository.PenaltyRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppointmentValidatorTest {

  @Mock
  private AppointmentRepository appointmentRepository;

  @Mock
  private PenaltyRepository penaltyRepository;

  @InjectMocks
  private AppointmentValidator appointmentValidator;

  @Test
  void validateAppointmentDateTime_shouldNotThrow_whenWeekdayTimeIsValid() {
    LocalDateTime dateTime = LocalDateTime.of(2026, 7, 6, 8, 0);

    assertDoesNotThrow(() -> appointmentValidator.validateAppointmentDateTime(dateTime));
  }

  @Test
  void validateAppointmentDateTime_shouldThrowException_whenMinuteIsInvalid() {
    LocalDateTime dateTime = LocalDateTime.of(2026, 7, 6, 8, 15);

    assertThrows(
        BusinessException.class,
        () -> appointmentValidator.validateAppointmentDateTime(dateTime)
    );
  }

  @Test
  void validateAppointmentDateTime_shouldThrowException_whenIsSunday() {
    LocalDateTime dateTime = LocalDateTime.of(2026, 7, 5, 8, 0);

    assertThrows(
        BusinessException.class,
        () -> appointmentValidator.validateAppointmentDateTime(dateTime)
    );
  }

  @Test
  void validateAppointmentDateTime_shouldThrowException_whenWeekdayTimeIsBeforeStart() {
    LocalDateTime dateTime = LocalDateTime.of(2026, 7, 6, 7, 30);

    assertThrows(
        BusinessException.class,
        () -> appointmentValidator.validateAppointmentDateTime(dateTime)
    );
  }

  @Test
  void validateAppointmentDateTime_shouldThrowException_whenWeekdayTimeIsAfterEnd() {
    LocalDateTime dateTime = LocalDateTime.of(2026, 7, 6, 18, 0);

    assertThrows(
        BusinessException.class,
        () -> appointmentValidator.validateAppointmentDateTime(dateTime)
    );
  }

  @Test
  void validateAppointmentDateTime_shouldNotThrow_whenSaturdayTimeIsValid() {
    LocalDateTime dateTime = LocalDateTime.of(2026, 7, 4, 8, 0);

    assertDoesNotThrow(() -> appointmentValidator.validateAppointmentDateTime(dateTime));
  }

  @Test
  void validateAppointmentDateTime_shouldThrowException_whenSaturdayTimeIsAfterEnd() {
    LocalDateTime dateTime = LocalDateTime.of(2026, 7, 4, 13, 0);

    assertThrows(
        BusinessException.class,
        () -> appointmentValidator.validateAppointmentDateTime(dateTime)
    );
  }

  @Test
  void validateDoctorAvailability_shouldThrowException_whenDoctorIsBusyOnCreate() {
    Long doctorId = 1L;
    LocalDateTime dateTime = LocalDateTime.of(2026, 7, 6, 9, 0);

    when(appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatus(
        doctorId,
        dateTime,
        AppointmentStatus.PROGRAMMED
    )).thenReturn(true);

    assertThrows(
        BusinessException.class,
        () -> appointmentValidator.validateDoctorAvailability(null, doctorId, dateTime)
    );
  }

  @Test
  void validateDoctorAvailability_shouldThrowException_whenDoctorIsBusyOnReschedule() {
    Long appointmentId = 5L;
    Long doctorId = 1L;
    LocalDateTime dateTime = LocalDateTime.of(2026, 7, 6, 9, 0);

    when(appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusAndIdNot(
        doctorId,
        dateTime,
        AppointmentStatus.PROGRAMMED,
        appointmentId
    )).thenReturn(true);

    assertThrows(
        BusinessException.class,
        () -> appointmentValidator.validateDoctorAvailability(appointmentId, doctorId, dateTime)
    );
  }

  @Test
  void validatePatientConflict_shouldThrowException_whenPatientHasConflictOnCreate() {
    Long patientId = 1L;
    Long doctorId = 1L;
    LocalDateTime dateTime = LocalDateTime.of(2026, 7, 6, 9, 0);

    when(appointmentRepository.existsByPatientIdAndDoctorIdAndAppointmentDateTimeAndStatus(
        patientId,
        doctorId,
        dateTime,
        AppointmentStatus.PROGRAMMED
    )).thenReturn(true);

    assertThrows(
        BusinessException.class,
        () -> appointmentValidator.validatePatientConflict(null, patientId, doctorId, dateTime)
    );
  }

  @Test
  void validatePatientConflict_shouldThrowException_whenPatientHasConflictOnReschedule() {
    Long appointmentId = 5L;
    Long patientId = 1L;
    Long doctorId = 1L;
    LocalDateTime dateTime = LocalDateTime.of(2026, 7, 6, 9, 0);

    when(appointmentRepository.existsByPatientIdAndDoctorIdAndAppointmentDateTimeAndStatusAndIdNot(
        patientId,
        doctorId,
        dateTime,
        AppointmentStatus.PROGRAMMED,
        appointmentId
    )).thenReturn(true);

    assertThrows(
        BusinessException.class,
        () -> appointmentValidator.validatePatientConflict(appointmentId, patientId, doctorId, dateTime)
    );
  }

  @Test
  void validatePatientPenalties_shouldThrowException_whenPatientHasThreeOrMorePenalties() {
    Long patientId = 1L;

    when(penaltyRepository.countByPatientIdAndPenaltyDateTimeAfter(
        eq(patientId),
        any(LocalDateTime.class)
    )).thenReturn(3L);

    assertThrows(
        BusinessException.class,
        () -> appointmentValidator.validatePatientPenalties(patientId)
    );
  }

  @Test
  void validatePatientPenalties_shouldNotThrow_whenPatientHasLessThanThreePenalties() {
    Long patientId = 1L;

    when(penaltyRepository.countByPatientIdAndPenaltyDateTimeAfter(
        eq(patientId),
        any(LocalDateTime.class)
    )).thenReturn(2L);

    assertDoesNotThrow(() -> appointmentValidator.validatePatientPenalties(patientId));
  }

}
