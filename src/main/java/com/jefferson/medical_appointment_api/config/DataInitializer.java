package com.jefferson.medical_appointment_api.config;

import com.jefferson.medical_appointment_api.entity.MedicalDoctorEntity;
import com.jefferson.medical_appointment_api.entity.PatientEntity;
import com.jefferson.medical_appointment_api.repository.MedicalDoctorRepository;
import com.jefferson.medical_appointment_api.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final MedicalDoctorRepository medicalDoctorRepository;
  private final PatientRepository patientRepository;

  @Override
  public void run(String... args) {

    loadDoctors();
    loadPatients();
  }

  private void loadDoctors() {

    if (medicalDoctorRepository.count() > 0) {
      return;
    }

    medicalDoctorRepository.save(MedicalDoctorEntity.builder()
        .fullName("Dr. Juan Pérez")
        .specialty("Cardiology")
        .phone("3001234567")
        .email("juan.perez@hospital.com")
        .build());

    medicalDoctorRepository.save(MedicalDoctorEntity.builder()
        .fullName("Dra. María Gómez")
        .specialty("Dermatology")
        .phone("3001234568")
        .email("maria.gomez@hospital.com")
        .build());

    medicalDoctorRepository.save(MedicalDoctorEntity.builder()
        .fullName("Dr. Carlos Ramírez")
        .specialty("Pediatrics")
        .phone("3001234569")
        .email("carlos.ramirez@hospital.com")
        .build());
  }

  private void loadPatients() {

    if (patientRepository.count() > 0) {
      return;
    }

    patientRepository.save(PatientEntity.builder()
        .fullName("Ana Torres")
        .document("1001001001")
        .phone("3101111111")
        .email("ana@gmail.com")
        .birthDate(LocalDate.of(1995, 5, 10))
        .build());

    patientRepository.save(PatientEntity.builder()
        .fullName("Pedro Martínez")
        .document("1001001002")
        .phone("3102222222")
        .email("pedro@gmail.com")
        .birthDate(LocalDate.of(1988, 8, 15))
        .build());

    patientRepository.save(PatientEntity.builder()
        .fullName("Laura Díaz")
        .document("1001001003")
        .phone("3103333333")
        .email("laura@gmail.com")
        .birthDate(LocalDate.of(2000, 2, 20))
        .build());
  }
}
