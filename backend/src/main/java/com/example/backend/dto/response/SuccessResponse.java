package com.example.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SuccessResponse<T>(
        String message,
        T data,
        String traceId,
        Instant timestamp
) {
    public static <T> SuccessResponse<T> of(String message, T data, String traceId) {
        return new SuccessResponse<>(message, data, traceId, Instant.now());
    }

    public static <T> SuccessResponse<T> of(T data, String traceId) {
        return new SuccessResponse<>(null, data, traceId, Instant.now());
    }
}