package com.jefferson.medical_appointment_api.validation;

import com.jefferson.medical_appointment_api.enums.AppointmentStatus;
import com.jefferson.medical_appointment_api.exception.BusinessException;
import com.jefferson.medical_appointment_api.repository.AppointmentRepository;
import com.jefferson.medical_appointment_api.repository.PenaltyRepository;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentValidator {

  private static final LocalTime WEEKDAY_START = LocalTime.of(8, 0);
  private static final LocalTime WEEKDAY_END = LocalTime.of(18, 0);
  private static final LocalTime SATURDAY_START = LocalTime.of(8, 0);
  private static final LocalTime SATURDAY_END = LocalTime.of(13, 0);
  private static final int MAX_PENALTIES = 3;
  private static final int PENALTY_DAYS_WINDOW = 30;

  private final AppointmentRepository appointmentRepository;
  private final PenaltyRepository penaltyRepository;


  public void validateAppointmentDateTime(LocalDateTime appointmentDateTime) {
    LocalTime appointmentTime = appointmentDateTime.toLocalTime();
    DayOfWeek dayOfWeek = appointmentDateTime.getDayOfWeek();

    if (appointmentTime.getMinute() != 0 && appointmentTime.getMinute() != 30) {
      throw new BusinessException("Appointments must be scheduled in 30-minute slots.");
    }

    if (dayOfWeek == DayOfWeek.SUNDAY) {
      throw new BusinessException("Appointments are not available on Sundays.");
    }

    if (dayOfWeek == DayOfWeek.SATURDAY) {
      validateSaturdaySchedule(appointmentTime);
      return;
    }

    validateWeekdaySchedule(appointmentTime);
  }

  public void validateDoctorAvailability(
      Long appointmentId,
      Long doctorId,
      LocalDateTime appointmentDateTime) {

    boolean exists = appointmentId == null
        ? appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatus(
        doctorId,
        appointmentDateTime,
        AppointmentStatus.PROGRAMMED
    )
        : appointmentRepository.existsByDoctorIdAndAppointmentDateTimeAndStatusAndIdNot(
            doctorId,
            appointmentDateTime,
            AppointmentStatus.PROGRAMMED,
            appointmentId
        );

    if (exists) {
      throw new BusinessException("The doctor already has an appointment at this time.");
    }
  }

  public void validatePatientConflict(
      Long appointmentId,
      Long patientId,
      Long doctorId,
      LocalDateTime appointmentDateTime) {

    boolean exists = appointmentId == null
        ? appointmentRepository.existsByPatientIdAndDoctorIdAndAppointmentDateTimeAndStatus(
        patientId,
        doctorId,
        appointmentDateTime,
        AppointmentStatus.PROGRAMMED
    )
        : appointmentRepository.existsByPatientIdAndDoctorIdAndAppointmentDateTimeAndStatusAndIdNot(
            patientId,
            doctorId,
            appointmentDateTime,
            AppointmentStatus.PROGRAMMED,
            appointmentId
        );

    if (exists) {
      throw new BusinessException("The patient already has an appointment with this doctor at this time.");
    }
  }

  private void validateSaturdaySchedule(LocalTime appointmentTime) {
    boolean validSaturdayTime =
        !appointmentTime.isBefore(SATURDAY_START)
            && appointmentTime.isBefore(SATURDAY_END);

    if (!validSaturdayTime) {
      throw new BusinessException("Saturday appointments are only available from 08:00 to 13:00.");
    }
  }

  private void validateWeekdaySchedule(LocalTime appointmentTime) {
    boolean validWeekdayTime =
        !appointmentTime.isBefore(WEEKDAY_START)
            && appointmentTime.isBefore(WEEKDAY_END);

    if (!validWeekdayTime) {
      throw new BusinessException("Weekday appointments are only available from 08:00 to 18:00.");
    }
  }

  public void validatePatientPenalties(Long patientId) {
    LocalDateTime fromDateTime = LocalDateTime.now().minusDays(PENALTY_DAYS_WINDOW);

    long penalties = penaltyRepository.countByPatientIdAndPenaltyDateTimeAfter(
        patientId,
        fromDateTime
    );

    if (penalties >= MAX_PENALTIES) {
      throw new BusinessException(
          "The patient cannot schedule appointments due to accumulated penalties in the last 30 days."
      );
    }
  }
}
