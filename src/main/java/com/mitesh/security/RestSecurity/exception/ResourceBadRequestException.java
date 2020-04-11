package com.mitesh.security.RestSecurity.exception;

public class ResourceBadRequestException extends Exception {

String traceId;
	
	public ResourceBadRequestException(String traceId,String message) {
		super(message);
		this.traceId=traceId;
	}

	public String getTraceId() {
		return traceId;
	}
	
}
