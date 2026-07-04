package com.jefferson.medical_appointment_api.controller;

import com.jefferson.medical_appointment_api.dto.request.AppointmentRequest;
import com.jefferson.medical_appointment_api.dto.request.RescheduleAppointmentRequest;
import com.jefferson.medical_appointment_api.dto.response.AppointmentResponse;
import com.jefferson.medical_appointment_api.dto.response.AvailableSlotResponse;
import com.jefferson.medical_appointment_api.enums.AppointmentStatus;
import com.jefferson.medical_appointment_api.service.AppointmentService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

  private final AppointmentService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AppointmentResponse createAppointment(@Valid @RequestBody AppointmentRequest request) {
    return service.createAppointment(request);
  }

  @GetMapping("/available-slots")
  public List<AvailableSlotResponse> getAvailableSlots(
      @RequestParam Long doctorId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    return service.getAvailableSlots(doctorId, startDate, endDate);
  }

  @PatchMapping("/{id}/cancel")
  public AppointmentResponse cancelAppointment(@PathVariable Long id) {
    return service.cancelAppointment(id);
  }

  @GetMapping
  public List<AppointmentResponse> getAppointments(
      @RequestParam(required = false) Long doctorId,
      @RequestParam(required = false) Long patientId,
      @RequestParam(required = false) AppointmentStatus status,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    return service.getAppointments(doctorId, patientId, status, startDate, endDate);
  }

  @PatchMapping("/{id}/reschedule")
  public AppointmentResponse rescheduleAppointment(
      @PathVariable Long id,
      @Valid @RequestBody RescheduleAppointmentRequest request) {

    return service.rescheduleAppointment(id, request);
  }
}
