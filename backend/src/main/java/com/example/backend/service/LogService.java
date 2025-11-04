package com.example.backend.service;

import com.example.backend.dto.LogMessage;
import com.example.backend.entity.Log;
import com.example.backend.repository.LogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    private final RabbitTemplate rabbitTemplate;
    private final LogRepository logRepository;
    private final ObjectMapper objectMapper;

    @Value("${rabbitmq.exchange.logs}")
    private String logsExchange;

    @Value("${rabbitmq.routing-key.logs}")
    private String logsRoutingKey;

    public LogService(RabbitTemplate rabbitTemplate, LogRepository logRepository, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.logRepository = logRepository;
        this.objectMapper = objectMapper;
    }

    // publish logs to rabbitmq

    public void logRequest(String traceId, String method, String path, Object body, String userId) {
        LogMessage message = LogMessage.builder()
                .traceId(traceId)
                .type("REQUEST")
                .method(method)
                .path(path)
                .requestBody(toJson(body))
                .userId(userId)
                .timestamp(Instant.now())
                .build();

        publishLog(message);
    }

    public void logResponse(String traceId, String method, String path,
                            Integer statusCode, Object body, Long durationMs, String userId) {
        LogMessage message = LogMessage.builder()
                .traceId(traceId)
                .type("RESPONSE")
                .method(method)
                .path(path)
                .statusCode(statusCode)
                .responseBody(toJson(body))
                .durationMs(durationMs)
                .userId(userId)
                .timestamp(Instant.now())
                .build();

        publishLog(message);
    }

    public void logError(String traceId, String method, String path,
                         Integer statusCode, String errorMessage, Long durationMs, String userId) {
        LogMessage message = LogMessage.builder()
                .traceId(traceId)
                .type("ERROR")
                .method(method)
                .path(path)
                .statusCode(statusCode)
                .responseBody(errorMessage)
                .durationMs(durationMs)
                .userId(userId)
                .timestamp(Instant.now())
                .build();

        publishLog(message);
    }

    private void publishLog(LogMessage message) {
        try {
            rabbitTemplate.convertAndSend(logsExchange, logsRoutingKey, message);
            logger.debug("Log published to RabbitMQ: {} {} [{}]", message.getMethod(), message.getPath(), message.getTraceId());
        } catch (Exception e) {
            logger.error("Failed to publish log to RabbitMQ: {} {} [{}]", message.getMethod(), message.getPath(), message.getTraceId(), e);
        }
    }

    // query logs

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