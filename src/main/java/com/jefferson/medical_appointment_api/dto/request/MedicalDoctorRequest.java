package com.jefferson.medical_appointment_api.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MedicalDoctorRequest(

    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    String fullName,

    @NotBlank(message = "Specialty is required")
    String specialty,

    @Pattern(regexp = "^[0-9\\-+()\\s]{7,20}$", message = "Phone must have at least 7 digits")
    String phone,

    @Email(message = "Email must be valid")
    String email
) {
}
