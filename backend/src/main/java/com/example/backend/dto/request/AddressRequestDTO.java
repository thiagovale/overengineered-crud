package com.example.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddressRequestDTO(
        Long id,

        @NotBlank(message = "Street is required")
        String street,

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "State is required")
        String state,

        @NotBlank(message = "Zip code is required")
        String zipCode
) {}