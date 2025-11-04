package com.example.backend.config;

import com.example.backend.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
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
        String path = request.getRequestURI();
        
        // Skip logging for Swagger/OpenAPI endpoints to avoid loops and unnecessary logs
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || 
            path.startsWith("/api-docs") || path.contains("swagger") || path.contains("api-docs")) {
            return;
        }
        
        String traceId = TraceContext.getTraceId();
        String method = request.getMethod();
        Integer statusCode = response.getStatus();

        Long startTime = (Long) request.getAttribute("startTime");
        Long durationMs = startTime != null ? System.currentTimeMillis() - startTime : null;

        String userId = getCurrentUserId();

        String requestBody = getRequestBody(request);
        logService.logRequest(traceId, method, path, requestBody, userId);

        String responseBody = getResponseBody(response);
        if (statusCode >= 400) {
            logService.logError(traceId, method, path, statusCode, responseBody, durationMs, userId);
        } else {
            logService.logResponse(traceId, method, path, statusCode, responseBody, durationMs, userId);
        }
    }

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof JWTUserData) {
            JWTUserData userData = (JWTUserData) auth.getPrincipal();
            Long userId = userData.userId();
            return userId != null ? userId.toString() : null;
        }

        return null;
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