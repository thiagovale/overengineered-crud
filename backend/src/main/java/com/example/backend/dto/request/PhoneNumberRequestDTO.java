package com.example.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados de telefone do cliente")
public record PhoneNumberRequestDTO(
        @Schema(description = "ID do telefone (usado apenas em atualizações)", example = "1")
        Long id,

        @Schema(description = "Número do telefone", example = "(11) 98765-4321", required = true)
        @NotBlank(message = "Number is required")
        String number,

        @Schema(description = "Tipo do telefone", example = "MOBILE", allowableValues = {"MOBILE", "HOME", "WORK"}, required = true)
        @NotBlank(message = "Type is required")
        String type
) {}