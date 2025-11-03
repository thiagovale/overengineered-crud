package com.example.backend.config;

import java.util.UUID;

public class TraceContext {
    private static final ThreadLocal<String> traceId = new ThreadLocal<>();

    public static void setTraceId(String id) {
        traceId.set(id);
    }

    public static String getTraceId() {
        String id = traceId.get();
        if (id == null) {
            id = UUID.randomUUID().toString();
            traceId.set(id);
        }
        return id;
    }

    public static void clear() {
        traceId.remove();
    }
}