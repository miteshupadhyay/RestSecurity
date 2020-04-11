package com.mitesh.security.RestSecurity.model.common;

public class PublisherApiError {

	private String traceId;
	private String errorMessage;
	
	public String getTraceId() {
		return traceId;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public PublisherApiError() {
		
	}
	public PublisherApiError(String traceId, String errorMessage) {
		super();
		this.traceId = traceId;
		this.errorMessage = errorMessage;
	}
	@Override
	public String toString() {
		return "PublisherApiError [traceId=" + traceId + ", errorMessage=" + errorMessage + "]";
	}
	
	
	
}
