package com.mitesh.security.RestSecurity.exception;

public class ResourceNotFoundException extends Exception {

String traceId;
	
	public ResourceNotFoundException(String traceId,String message) {
		super(message);
		this.traceId=traceId;
	}

	public String getTraceId() {
		return traceId;
	}
	
}
