package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogMessage {
    private String traceId;
    private String type;
    private String method;
    private String path;
    private Integer statusCode;
    private String requestBody;
    private String responseBody;
    private Long durationMs;
    private String userId;
    private Instant timestamp;
}