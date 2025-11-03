package com.example.backend.dto.response;

import java.time.Instant;

public record ErrorResponse(
        String message,
        String error,
        String traceId,
        Instant timestamp
) {
    public static ErrorResponse of(String message, String error, String traceId) {
        return new ErrorResponse(message, error, traceId, Instant.now());
    }
}