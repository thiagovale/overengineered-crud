package com.example.backend.service;

import com.example.backend.entity.Log;
import com.example.backend.repository.LogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    private final LogRepository logRepository;
    private final ObjectMapper objectMapper;

    public LogService(LogRepository logRepository, ObjectMapper objectMapper) {
        this.logRepository = logRepository;
        this.objectMapper = objectMapper;
    }

    @Async
    public void logRequest(String traceId, String method, String path, Object body, String userId) {
        Log log = new Log();
        log.setTraceId(traceId);
        log.setType("REQUEST");
        log.setMethod(method);
        log.setPath(path);
        log.setRequestBody(toJson(body));
        log.setUserId(userId);
        log.setTimestamp(Instant.now());

        logRepository.save(log);
    }

    @Async
    public void logResponse(String traceId, String method, String path,
                            Integer statusCode, Object body, Long durationMs, String userId) {
        Log log = new Log();
        log.setTraceId(traceId);
        log.setType("RESPONSE");
        log.setMethod(method);
        log.setPath(path);
        log.setStatusCode(statusCode);
        log.setResponseBody(toJson(body));
        log.setDurationMs(durationMs);
        log.setUserId(userId);
        log.setTimestamp(Instant.now());

        logRepository.save(log);
    }

    @Async
    public void logError(String traceId, String method, String path,
                         Integer statusCode, String errorMessage, Long durationMs, String userId) {
        Log log = new Log();
        log.setTraceId(traceId);
        log.setType("ERROR");
        log.setMethod(method);
        log.setPath(path);
        log.setStatusCode(statusCode);
        log.setResponseBody(errorMessage);
        log.setDurationMs(durationMs);
        log.setUserId(userId);
        log.setTimestamp(Instant.now());

        logRepository.save(log);
    }

    public List<Log> findByTraceId(String traceId) {
        return logRepository.findByTraceIdOrderByTimestampAsc(traceId);
    }

    public List<Log> findByType(String type) {
        return logRepository.findByTypeOrderByTimestampDesc(type);
    }

    public List<Log> findByDateRange(Instant startDate, Instant endDate) {
        return logRepository.findByTimestampBetweenOrderByTimestampDesc(startDate, endDate);
    }

    public List<Log> findByPath(String path) {
        return logRepository.findByPathContainingOrderByTimestampDesc(path);
    }

    private String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }
}