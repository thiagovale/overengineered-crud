package com.example.backend.dto.response;

public record AddressResponseDTO(
        Long id,
        String street,
        String city,
        String state,
        String zipCode
) {}