package com.jefferson.medical_appointment_api.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentRequest(

    @NotNull(message = "Patient id is required")
    Long patientId,

    @NotNull(message = "Doctor id is required")
    Long doctorId,

    @NotNull(message = "Appointment date time is required")
    @FutureOrPresent(message = "Appointment date time must be now or in the future")
    LocalDateTime appointmentDateTime
) {
}
