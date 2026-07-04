package com.jefferson.medical_appointment_api.repository;


import com.jefferson.medical_appointment_api.entity.PenaltyEntity;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenaltyRepository extends JpaRepository<PenaltyEntity, Long> {

  long countByPatientIdAndPenaltyDateTimeAfter(
      Long patientId,
      LocalDateTime fromDateTime
  );
}