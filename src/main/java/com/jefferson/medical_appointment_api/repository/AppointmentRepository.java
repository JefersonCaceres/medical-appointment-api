package com.jefferson.medical_appointment_api.repository;

import com.jefferson.medical_appointment_api.entity.AppointmentEntity;
import com.jefferson.medical_appointment_api.enums.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

  boolean existsByDoctorIdAndAppointmentDateTimeAndStatus(
      Long doctorId,
      LocalDateTime appointmentDateTime,
      AppointmentStatus status
  );

  boolean existsByPatientIdAndDoctorIdAndAppointmentDateTimeAndStatus(
      Long patientId,
      Long doctorId,
      LocalDateTime appointmentDateTime,
      AppointmentStatus status
  );

  List<AppointmentEntity> findByDoctorIdAndAppointmentDateTimeBetweenAndStatus(
      Long doctorId,
      LocalDateTime startDateTime,
      LocalDateTime endDateTime,
      AppointmentStatus status
  );
}