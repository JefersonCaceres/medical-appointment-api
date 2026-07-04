package com.jefferson.medical_appointment_api.validation;

import com.jefferson.medical_appointment_api.dto.response.AvailableSlotResponse;
import com.jefferson.medical_appointment_api.entity.AppointmentEntity;
import com.jefferson.medical_appointment_api.enums.AppointmentStatus;
import com.jefferson.medical_appointment_api.repository.AppointmentRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentAvailability {

  private static final LocalTime WEEKDAY_START = LocalTime.of(8, 0);
  private static final LocalTime WEEKDAY_END = LocalTime.of(18, 0);
  private static final LocalTime SATURDAY_START = LocalTime.of(8, 0);
  private static final LocalTime SATURDAY_END = LocalTime.of(13, 0);
  private static final int SLOT_MINUTES = 30;

  private final AppointmentRepository appointmentRepository;

  public List<AvailableSlotResponse> generateAvailableSlots(
      Long doctorId,
      LocalDate startDate,
      LocalDate endDate) {

    LocalDateTime startDateTime = startDate.atStartOfDay();
    LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

    Set<LocalDateTime> occupiedSlots = appointmentRepository
        .findByDoctorIdAndAppointmentDateTimeBetweenAndStatus(
            doctorId,
            startDateTime,
            endDateTime,
            AppointmentStatus.PROGRAMMED
        )
        .stream()
        .map(AppointmentEntity::getAppointmentDateTime)
        .collect(Collectors.toSet());

    return startDate.datesUntil(endDate.plusDays(1))
        .filter(this::isWorkingDay)
        .flatMap(date -> generateSlotsForDate(date).stream())
        .filter(slot -> !occupiedSlots.contains(slot))
        .map(slot -> new AvailableSlotResponse(slot, slot.plusMinutes(SLOT_MINUTES)))
        .toList();
  }

  private boolean isWorkingDay(LocalDate date) {
    return date.getDayOfWeek() != DayOfWeek.SUNDAY;
  }

  private List<LocalDateTime> generateSlotsForDate(LocalDate date) {
    LocalTime startTime = getStartTime(date);
    LocalTime endTime = getEndTime(date);

    return Stream.iterate(
            startTime,
            time -> time.isBefore(endTime),
            time -> time.plusMinutes(SLOT_MINUTES)
        ).map(date::atTime)
        .toList();
  }

  private LocalTime getStartTime(LocalDate date) {
    return date.getDayOfWeek() == DayOfWeek.SATURDAY
        ? SATURDAY_START
        : WEEKDAY_START;
  }

  private LocalTime getEndTime(LocalDate date) {
    return date.getDayOfWeek() == DayOfWeek.SATURDAY
        ? SATURDAY_END
        : WEEKDAY_END;
  }
}