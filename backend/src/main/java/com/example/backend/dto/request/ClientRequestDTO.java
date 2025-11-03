package com.example.backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record ClientRequestDTO(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Document number is required")
        String documentNumber,

        @Valid
        List<AddressRequestDTO> addresses,

        @Valid
        List<PhoneNumberRequestDTO> phoneNumbers
) {}