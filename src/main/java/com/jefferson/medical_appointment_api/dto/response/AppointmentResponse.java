package com.jefferson.medical_appointment_api.dto.response;

import com.jefferson.medical_appointment_api.enums.AppointmentStatus;
import java.time.LocalDateTime;

public record AppointmentResponse(
    Long id,
    Long patientId,
    String patientName,
    Long doctorId,
    String doctorName,
    LocalDateTime appointmentDateTime,
    AppointmentStatus status,
    LocalDateTime cancellationDateTime
) {
}
