package com.mitesh.security.RestSecurity.exception;
public class ResourceUnauthorizedException extends Exception {

    private String traceId;

    public ResourceUnauthorizedException(String traceId, String message) {
        super(message);
        this.traceId = traceId;
    }

    public String getTraceId() {
        return traceId;
    }
}