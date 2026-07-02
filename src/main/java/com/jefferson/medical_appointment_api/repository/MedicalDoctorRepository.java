package com.jefferson.medical_appointment_api.repository;

import com.jefferson.medical_appointment_api.entity.MedicalDoctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalDoctorRepository extends JpaRepository<MedicalDoctor, Long> {
}
