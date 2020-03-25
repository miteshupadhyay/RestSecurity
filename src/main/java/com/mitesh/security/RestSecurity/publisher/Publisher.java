package com.mitesh.security.RestSecurity.publisher;

public class Publisher {

	private Integer publisherId;
	private String name;
	private String emailId;
	private String phoneNumber;
	
	public Publisher() {
		
	}
	public Integer getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(Integer publisherId) {
		this.publisherId = publisherId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Publisher(Integer publisherId, String name, String emailId, String phoneNumber) {
		super();
		this.publisherId = publisherId;
		this.name = name;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
	}
	
	
	
}
