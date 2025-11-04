package com.example.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Dados para registro de novo usuário")
public record RegisterUserRequest(
        @Schema(description = "Nome de usuário único", example = "joao", required = true)
        @NotEmpty(message = "Usuário é obrigatório") 
        String username,
        
        @Schema(description = "Senha do usuário", example = "senha123", required = true)
        @NotEmpty(message = "Senha é obrigatória") 
        String password
) {}
