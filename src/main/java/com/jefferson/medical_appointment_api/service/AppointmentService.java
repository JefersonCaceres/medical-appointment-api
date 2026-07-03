package com.jefferson.medical_appointment_api.service;
import com.jefferson.medical_appointment_api.dto.request.AppointmentRequest;
import com.jefferson.medical_appointment_api.dto.response.AppointmentResponse;
import com.jefferson.medical_appointment_api.dto.response.AvailableSlotResponse;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

  AppointmentResponse createAppointment(AppointmentRequest request);
  List<AvailableSlotResponse> getAvailableSlots(Long doctorId, LocalDate startDate, LocalDate endDate);
  AppointmentResponse cancelAppointment(Long appointmentId);
}
