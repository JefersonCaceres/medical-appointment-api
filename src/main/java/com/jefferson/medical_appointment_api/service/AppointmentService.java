package com.jefferson.medical_appointment_api.service;
import com.jefferson.medical_appointment_api.dto.request.AppointmentRequest;
import com.jefferson.medical_appointment_api.dto.request.RescheduleAppointmentRequest;
import com.jefferson.medical_appointment_api.dto.response.AppointmentResponse;
import com.jefferson.medical_appointment_api.dto.response.AvailableSlotResponse;
import com.jefferson.medical_appointment_api.enums.AppointmentStatus;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

  AppointmentResponse createAppointment(AppointmentRequest request);
  List<AvailableSlotResponse> getAvailableSlots(Long doctorId, LocalDate startDate, LocalDate endDate);
  AppointmentResponse cancelAppointment(Long appointmentId);
  List<AppointmentResponse> getAppointments(Long doctorId, Long patientId, AppointmentStatus status, LocalDate startDate, LocalDate endDate);
  AppointmentResponse rescheduleAppointment(Long appointmentId, RescheduleAppointmentRequest request
  );
}
