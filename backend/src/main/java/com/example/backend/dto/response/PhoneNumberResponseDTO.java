package com.example.backend.dto.response;

public record PhoneNumberResponseDTO(
        Long id,
        String number,
        String type
) {}