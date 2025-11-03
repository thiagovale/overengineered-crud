package com.example.backend.dto.response;

import java.time.Instant;
import java.util.Map;

public record ValidationErrorResponse(
        String message,
        Map<String, String> errors,
        String traceId,
        Instant timestamp
) {
    public static ValidationErrorResponse of(String message, Map<String, String> errors, String traceId) {
        return new ValidationErrorResponse(message, errors, traceId, Instant.now());
    }
}