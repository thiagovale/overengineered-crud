package com.example.backend.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record RegisterUserRequest(@NotEmpty(message = "Usuário é obrigatório") String username,
                                  @NotEmpty(message = "Senha é obrigatória") String password
) {}
