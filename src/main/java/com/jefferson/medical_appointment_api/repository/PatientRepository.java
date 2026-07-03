package com.jefferson.medical_appointment_api.repository;

import com.jefferson.medical_appointment_api.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {

  boolean existsByDocument(String document);
}
