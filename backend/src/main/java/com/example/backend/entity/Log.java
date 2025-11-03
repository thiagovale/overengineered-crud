package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trace_id", nullable = false)
    private String traceId;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(length = 10)
    private String method;

    @Column(length = 500)
    private String path;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "request_body", columnDefinition = "TEXT")
    private String requestBody;

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(nullable = false)
    private Instant timestamp;
}
