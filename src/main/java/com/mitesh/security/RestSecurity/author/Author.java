package com.mitesh.security.RestSecurity.author;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.mitesh.security.RestSecurity.model.common.Gender;

public class Author {

	private Integer authorId;
	
	@NotNull
	@Size(min = 1,max = 50,message = "FirstName must be in between 1 to 50 characters")
	private String firstName;
	
	@NotNull
	@Size(min = 1,max = 50,message = "LastName must be in between 1 to 50 characters")
	private String lastName;
	
	@Past(message = "Date Of Birth should be Past Date")
	private LocalDate dateOfBirth;
	
	private Gender gender;

	
	public Author() {
	}
	
	public Author(Integer authorId, String firstName, String lastName, LocalDate dateOfBirth, Gender gender) {
		this.authorId = authorId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
	}

	public Author(Integer authorId, String firstName, String lastName) {
		this.authorId = authorId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "Author [authorId=" + authorId + ", firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth="
				+ dateOfBirth + "]";
	}
	
	
}
