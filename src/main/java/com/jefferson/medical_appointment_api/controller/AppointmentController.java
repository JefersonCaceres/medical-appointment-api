package com.jefferson.medical_appointment_api.controller;

import com.jefferson.medical_appointment_api.dto.request.AppointmentRequest;
import com.jefferson.medical_appointment_api.dto.response.AppointmentResponse;
import com.jefferson.medical_appointment_api.dto.response.AvailableSlotResponse;
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
}
