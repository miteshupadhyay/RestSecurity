package com.mitesh.security.RestSecurity.exception;

public class ResourceAlreadyExistsException extends Exception {

	String traceId;
	
	public ResourceAlreadyExistsException(String traceId,String message) {
		super(message);
		this.traceId=traceId;
	}

	public String getTraceId() {
		return traceId;
	}
	

}
