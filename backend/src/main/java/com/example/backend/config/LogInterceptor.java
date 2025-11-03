package com.example.backend.config;

import com.example.backend.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;

@Component
public class LogInterceptor implements HandlerInterceptor {

    private final LogService logService;

    public LogInterceptor(LogService logService) {
        this.logService = logService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        String traceId = TraceContext.getTraceId();
        String method = request.getMethod();
        String path = request.getRequestURI();
        Integer statusCode = response.getStatus();

        Long startTime = (Long) request.getAttribute("startTime");
        Long durationMs = startTime != null ? System.currentTimeMillis() - startTime : null;


        String requestBody = getRequestBody(request);
        logService.logRequest(traceId, method, path, requestBody);


        String responseBody = getResponseBody(response);
        if (statusCode >= 400) {
            logService.logError(traceId, method, path, statusCode, responseBody, durationMs);
        } else {
            logService.logResponse(traceId, method, path, statusCode, responseBody, durationMs);
        }
    }

    private String getRequestBody(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                return new String(buf, 0, buf.length, StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    private String getResponseBody(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                return new String(buf, 0, buf.length, StandardCharsets.UTF_8);
            }
        }
        return null;
    }
}