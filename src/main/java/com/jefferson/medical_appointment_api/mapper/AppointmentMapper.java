package com.jefferson.medical_appointment_api.mapper;

import com.jefferson.medical_appointment_api.dto.response.AppointmentResponse;
import com.jefferson.medical_appointment_api.entity.AppointmentEntity;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

  public AppointmentResponse toResponse(AppointmentEntity appointment) {
    return new AppointmentResponse(
        appointment.getId(),
        appointment.getPatient().getId(),
        appointment.getPatient().getFullName(),
        appointment.getDoctor().getId(),
        appointment.getDoctor().getFullName(),
        appointment.getAppointmentDateTime(),
        appointment.getStatus(),
        appointment.getCancellationDateTime()
    );
  }
}
