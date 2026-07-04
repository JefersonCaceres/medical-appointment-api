package com.jefferson.medical_appointment_api.repository;

import com.jefferson.medical_appointment_api.entity.AppointmentEntity;
import com.jefferson.medical_appointment_api.enums.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long>,
    JpaSpecificationExecutor<AppointmentEntity> {

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

  boolean existsByDoctorIdAndAppointmentDateTimeAndStatusAndIdNot(
      Long doctorId,
      LocalDateTime appointmentDateTime,
      AppointmentStatus status,
      Long appointmentId
  );

  boolean existsByPatientIdAndDoctorIdAndAppointmentDateTimeAndStatusAndIdNot(
      Long patientId,
      Long doctorId,
      LocalDateTime appointmentDateTime,
      AppointmentStatus status,
      Long appointmentId
  );
}