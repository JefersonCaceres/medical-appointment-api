package com.jefferson.medical_appointment_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record PatientRequest(

    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    String fullName,

    @NotBlank(message = "Document is required")
    @Size(min = 7, max = 30, message = "Document must be between 7 and 30 characters")
    String document,

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9\\-+()\\s]{7,20}$", message = "Phone must have at least 7 digits")
    String phone,

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,

    @PastOrPresent(message = "Birth date cannot be in the future")
    LocalDate birthDate
) {
}
