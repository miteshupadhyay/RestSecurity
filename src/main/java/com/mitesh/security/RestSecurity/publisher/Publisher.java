package com.mitesh.security.RestSecurity.publisher;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Publisher {

	private Integer publisherId;
	
	@Size(min = 1,max = 50,message = "Publisher Name must be in between 1 and 50")
	private String name;
	
	@Email(message = "Please enter a valid Email Id")
	private String emailId;
	
	@Pattern(regexp = "\\d{3}-\\d{3}-\\d{3}",message = "Please enter a valid phone number")
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
