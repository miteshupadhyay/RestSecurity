package com.mitesh.security.RestSecurity.author;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.mitesh.security.RestSecurity.model.common.Gender;

@Entity
@Table(name = "AUTHOR")
public class AuthorEntity {

	@Column(name = "Author_Id")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "authorId_generator")
	@SequenceGenerator(name ="authorId_generator",sequenceName = "author_sequence",allocationSize = 50)
	private int authorId;
	
	@Column(name = "First_Name")
	private String firstName;
	
	@Column(name = "Last_Name")
	private String lastName;
	
	@Column(name = "Date_Of_Birth")
	private LocalDate dateOfBirth;
	
	@Column(name = "Gender")
	@Enumerated(EnumType.STRING)
	private Gender gender;

	public int getAuthorId() {
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

	public void setAuthorId(int authorId) {
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
	public AuthorEntity() {
		
	}
	public AuthorEntity(String firstName, String lastName, LocalDate dateOfBirth, Gender gender) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
	}
	
	
	
}
