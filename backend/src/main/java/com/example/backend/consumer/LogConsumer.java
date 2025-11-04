package com.example.backend.consumer;

import com.example.backend.dto.LogMessage;
import com.example.backend.entity.Log;
import com.example.backend.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LogConsumer {

    private static final Logger logger = LoggerFactory.getLogger(LogConsumer.class);

    private final LogRepository logRepository;

    public LogConsumer(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.logs}")
    public void consumeLog(LogMessage message) {
        try {
            Log log = new Log();
            log.setTraceId(message.getTraceId());
            log.setType(message.getType());
            log.setMethod(message.getMethod());
            log.setPath(message.getPath());
            log.setStatusCode(message.getStatusCode());
            log.setRequestBody(message.getRequestBody());
            log.setResponseBody(message.getResponseBody());
            log.setDurationMs(message.getDurationMs());
            log.setUserId(message.getUserId());
            log.setTimestamp(message.getTimestamp());

            logRepository.save(log);
            logger.debug("Log saved from RabbitMQ: {} {} [{}]", message.getMethod(), message.getPath(), message.getTraceId());
        } catch (Exception e) {
            logger.error("Failed to save log from RabbitMQ: {} {} [{}]", message.getMethod(), message.getPath(), message.getTraceId(), e);
            throw e;
        }
    }
}