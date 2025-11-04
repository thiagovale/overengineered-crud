package com.example.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Dados para criação ou atualização de um cliente")
public record ClientRequestDTO(
        @Schema(description = "Primeiro nome do cliente", example = "João", required = true)
        @NotBlank(message = "First name is required")
        String firstName,

        @Schema(description = "Sobrenome do cliente", example = "Silva", required = true)
        @NotBlank(message = "Last name is required")
        String lastName,

        @Schema(description = "Data de nascimento do cliente", example = "1990-01-15", required = true)
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @Schema(description = "Email do cliente", example = "joao.silva@example.com", required = true)
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @Schema(description = "CPF ou documento do cliente", example = "12345678900", required = true)
        @NotBlank(message = "Document number is required")
        String documentNumber,

        @Schema(description = "Lista de endereços do cliente")
        @Valid
        List<AddressRequestDTO> addresses,

        @Schema(description = "Lista de telefones do cliente")
        @Valid
        List<PhoneNumberRequestDTO> phoneNumbers
) {}