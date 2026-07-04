package com.jefferson.medical_appointment_api.dto.response;

import java.time.LocalDateTime;

public record AvailableSlotResponse(
    LocalDateTime startDateTime,
    LocalDateTime endDateTime
) {
}