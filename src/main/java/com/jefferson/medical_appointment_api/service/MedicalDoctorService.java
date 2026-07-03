package com.jefferson.medical_appointment_api.service;

import com.jefferson.medical_appointment_api.dto.request.MedicalDoctorRequest;
import com.jefferson.medical_appointment_api.dto.response.MedicalDoctorResponse;
import java.util.List;

public interface MedicalDoctorService {

  MedicalDoctorResponse createMedicalDoctor(MedicalDoctorRequest request);

  List<MedicalDoctorResponse> getAllMedicalDoctors();

  MedicalDoctorResponse getMedicalDoctorById(Long id);
}
