package com.mitesh.security.RestSecurity.model;

public class Publisher {

	private String publisherId;
	private String name;
	private String emailId;
	private String phoneNumber;
	
	public Publisher() {
		
	}
	public String getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(String publisherId) {
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
	public Publisher(String publisherId, String name, String emailId, String phoneNumber) {
		super();
		this.publisherId = publisherId;
		this.name = name;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
	}
	
	
	
}
