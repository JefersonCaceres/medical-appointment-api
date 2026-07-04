package com.jefferson.medical_appointment_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.jefferson.medical_appointment_api.dto.request.MedicalDoctorRequest;
import com.jefferson.medical_appointment_api.dto.response.MedicalDoctorResponse;
import com.jefferson.medical_appointment_api.entity.MedicalDoctorEntity;
import com.jefferson.medical_appointment_api.exception.ResourceNotFoundException;
import com.jefferson.medical_appointment_api.mapper.MedicalDoctorMapper;
import com.jefferson.medical_appointment_api.repository.MedicalDoctorRepository;
import com.jefferson.medical_appointment_api.service.impl.MedicalDoctorServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MedicalDoctorServiceImplTest {

  @Mock
  private MedicalDoctorRepository repository;

  @Mock
  private MedicalDoctorMapper mapper;

  @InjectMocks
  private MedicalDoctorServiceImpl service;

  @Test
  void createMedicalDoctor_shouldReturnCreatedDoctor() {
    MedicalDoctorRequest request = new MedicalDoctorRequest(
        "Dra. María González",
        "Cardiología",
        "555-1001",
        "maria.gonzalez@medisalud.com"
    );

    MedicalDoctorEntity entity = MedicalDoctorEntity.builder()
        .fullName(request.fullName())
        .specialty(request.specialty())
        .phone(request.phone())
        .email(request.email())
        .build();

    MedicalDoctorEntity savedEntity = MedicalDoctorEntity.builder()
        .id(1L)
        .fullName(request.fullName())
        .specialty(request.specialty())
        .phone(request.phone())
        .email(request.email())
        .build();

    MedicalDoctorResponse response = new MedicalDoctorResponse(
        1L,
        "Dra. María González",
        "Cardiología",
        "555-1001",
        "maria.gonzalez@medisalud.com"
    );

    when(mapper.toEntity(request)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(savedEntity);
    when(mapper.toResponse(savedEntity)).thenReturn(response);

    MedicalDoctorResponse result = service.createMedicalDoctor(request);

    assertEquals(1L, result.id());
    assertEquals("Dra. María González", result.fullName());
    assertEquals("Cardiología", result.specialty());
  }

  @Test
  void getMedicalDoctorById_shouldReturnDoctor_whenDoctorExists() {
    MedicalDoctorEntity entity = MedicalDoctorEntity.builder()
        .id(1L)
        .fullName("Dra. María González")
        .specialty("Cardiología")
        .phone("555-1001")
        .email("maria.gonzalez@medisalud.com")
        .build();

    MedicalDoctorResponse response = new MedicalDoctorResponse(
        1L,
        "Dra. María González",
        "Cardiología",
        "555-1001",
        "maria.gonzalez@medisalud.com"
    );

    when(repository.findById(1L)).thenReturn(Optional.of(entity));
    when(mapper.toResponse(entity)).thenReturn(response);

    MedicalDoctorResponse result = service.getMedicalDoctorById(1L);

    assertEquals(1L, result.id());
    assertEquals("Dra. María González", result.fullName());
  }

  @Test
  void getMedicalDoctorById_shouldThrowException_whenDoctorDoesNotExist() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> service.getMedicalDoctorById(99L)
    );
  }
}
