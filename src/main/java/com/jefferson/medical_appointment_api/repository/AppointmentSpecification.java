package com.jefferson.medical_appointment_api.repository;

import com.jefferson.medical_appointment_api.entity.AppointmentEntity;
import com.jefferson.medical_appointment_api.enums.AppointmentStatus;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

public class AppointmentSpecification {

  private AppointmentSpecification() {
  }

  public static Specification<AppointmentEntity> hasDoctorId(Long doctorId) {
    return (root, query, criteriaBuilder) ->
        doctorId == null
            ? criteriaBuilder.conjunction()
            : criteriaBuilder.equal(root.get("doctor").get("id"), doctorId);
  }

  public static Specification<AppointmentEntity> hasPatientId(Long patientId) {
    return (root, query, criteriaBuilder) ->
        patientId == null
            ? criteriaBuilder.conjunction()
            : criteriaBuilder.equal(root.get("patient").get("id"), patientId);
  }

  public static Specification<AppointmentEntity> hasStatus(AppointmentStatus status) {
    return (root, query, criteriaBuilder) ->
        status == null
            ? criteriaBuilder.conjunction()
            : criteriaBuilder.equal(root.get("status"), status);
  }

  public static Specification<AppointmentEntity> appointmentDateTimeBetween(
      LocalDateTime startDateTime,
      LocalDateTime endDateTime) {

    return (root, query, criteriaBuilder) -> {
      if (startDateTime == null && endDateTime == null) {
        return criteriaBuilder.conjunction();
      }

      if (startDateTime != null && endDateTime != null) {
        return criteriaBuilder.between(root.get("appointmentDateTime"), startDateTime, endDateTime);
      }

      if (startDateTime != null) {
        return criteriaBuilder.greaterThanOrEqualTo(root.get("appointmentDateTime"), startDateTime);
      }

      return criteriaBuilder.lessThanOrEqualTo(root.get("appointmentDateTime"), endDateTime);
    };
  }
}
