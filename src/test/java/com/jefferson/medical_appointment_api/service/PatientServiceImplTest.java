package com.jefferson.medical_appointment_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.jefferson.medical_appointment_api.dto.request.PatientRequest;
import com.jefferson.medical_appointment_api.dto.response.PatientResponse;
import com.jefferson.medical_appointment_api.entity.PatientEntity;
import com.jefferson.medical_appointment_api.exception.BusinessException;
import com.jefferson.medical_appointment_api.exception.ResourceNotFoundException;
import com.jefferson.medical_appointment_api.mapper.PatientMapper;
import com.jefferson.medical_appointment_api.repository.PatientRepository;
import com.jefferson.medical_appointment_api.service.impl.PatientServiceImpl;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

  @Mock
  private PatientRepository repository;

  @Mock
  private PatientMapper mapper;

  @InjectMocks
  private PatientServiceImpl service;

  @Test
  void createPatient_shouldReturnCreatedPatient() {
    PatientRequest request = new PatientRequest(
        "Juan Pérez",
        "123456789",
        "555-2001",
        "juan@email.com",
        LocalDate.of(1995, 4, 20)
    );

    PatientEntity entity = PatientEntity.builder()
        .fullName(request.fullName())
        .document(request.document())
        .phone(request.phone())
        .email(request.email())
        .birthDate(request.birthDate())
        .build();

    PatientEntity savedEntity = PatientEntity.builder()
        .id(1L)
        .fullName(request.fullName())
        .document(request.document())
        .phone(request.phone())
        .email(request.email())
        .birthDate(request.birthDate())
        .build();

    PatientResponse response = new PatientResponse(
        1L,
        "Juan Pérez",
        "123456789",
        "555-2001",
        "juan@email.com",
        LocalDate.of(1995, 4, 20)
    );

    when(repository.existsByDocument(request.document())).thenReturn(false);
    when(mapper.toEntity(request)).thenReturn(entity);
    when(repository.save(entity)).thenReturn(savedEntity);
    when(mapper.toResponse(savedEntity)).thenReturn(response);

    PatientResponse result = service.createPatient(request);

    assertEquals(1L, result.id());
    assertEquals("Juan Pérez", result.fullName());
    assertEquals("123456789", result.document());
  }

  @Test
  void createPatient_shouldThrowException_whenDocumentAlreadyExists() {
    PatientRequest request = new PatientRequest(
        "Juan Pérez",
        "123456789",
        "555-2001",
        "juan@email.com",
        LocalDate.of(1995, 4, 20)
    );

    when(repository.existsByDocument(request.document())).thenReturn(true);

    assertThrows(
        BusinessException.class,
        () -> service.createPatient(request)
    );
  }

  @Test
  void getPatientById_shouldThrowException_whenPatientDoesNotExist() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> service.getPatientById(99L)
    );
  }
}