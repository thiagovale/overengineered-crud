package com.example.backend.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ClientResponseDTO(
        Long id,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String email,
        String documentNumber,
        List<AddressResponseDTO> addresses,
        List<PhoneNumberResponseDTO> phoneNumbers
) {}