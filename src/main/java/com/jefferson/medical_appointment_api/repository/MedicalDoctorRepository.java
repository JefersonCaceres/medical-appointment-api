package com.jefferson.medical_appointment_api.repository;

import com.jefferson.medical_appointment_api.entity.MedicalDoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalDoctorRepository extends JpaRepository<MedicalDoctorEntity, Long> {
}
