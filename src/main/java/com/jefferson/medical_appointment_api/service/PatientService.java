package com.jefferson.medical_appointment_api.service;

import com.jefferson.medical_appointment_api.dto.request.PatientRequest;
import com.jefferson.medical_appointment_api.dto.response.PatientResponse;
import java.util.List;

public interface PatientService {
  PatientResponse createPatient(PatientRequest request);

  List<PatientResponse> getAllPatients();

  PatientResponse getPatientById(Long id);
}
