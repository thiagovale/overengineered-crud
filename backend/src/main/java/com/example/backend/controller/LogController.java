package com.example.backend.controller;

import com.example.backend.config.TraceContext;
import com.example.backend.dto.response.SuccessResponse;
import com.example.backend.entity.Log;
import com.example.backend.service.LogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<Log>>> getLogs(
            @RequestParam(required = false) String traceId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String path,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate
    ) {
        List<Log> logs;

        if (traceId != null) {
            logs = logService.findByTraceId(traceId);
        } else if (type != null) {
            logs = logService.findByType(type);
        } else if (path != null) {
            logs = logService.findByPath(path);
        } else if (startDate != null && endDate != null) {
            logs = logService.findByDateRange(startDate, endDate);
        } else {
            logs = List.of();
        }

        return ResponseEntity.ok(
                SuccessResponse.of("Logs retrieved successfully", logs, TraceContext.getTraceId())
        );
    }

    @GetMapping("/trace/{traceId}")
    public ResponseEntity<SuccessResponse<List<Log>>> getLogsByTraceId(@PathVariable String traceId) {
        List<Log> logs = logService.findByTraceId(traceId);
        return ResponseEntity.ok(
                SuccessResponse.of("Logs retrieved successfully", logs, TraceContext.getTraceId())
        );
    }
}