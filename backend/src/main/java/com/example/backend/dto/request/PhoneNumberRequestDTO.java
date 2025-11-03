package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PhoneNumberRequestDTO(
        Long id,

        @NotBlank(message = "Number is required")
        String number,

        @NotBlank(message = "Type is required")
        String type
) {}