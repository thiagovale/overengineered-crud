package com.example.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados de endereço do cliente")
public record AddressRequestDTO(
        @Schema(description = "ID do endereço (usado apenas em atualizações)", example = "1")
        Long id,

        @Schema(description = "Logradouro", example = "Rua das Flores, 123", required = true)
        @NotBlank(message = "Street is required")
        String street,

        @Schema(description = "Cidade", example = "São Paulo", required = true)
        @NotBlank(message = "City is required")
        String city,

        @Schema(description = "Estado", example = "SP", required = true)
        @NotBlank(message = "State is required")
        String state,

        @Schema(description = "CEP", example = "01234-567", required = true)
        @NotBlank(message = "Zip code is required")
        String zipCode
) {}