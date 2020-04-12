package com.mitesh.security.RestSecurity.author;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<AuthorEntity, Integer>{

		List<AuthorEntity> findByFirstNameContaining(String firstName);
		
		List<AuthorEntity> findByLastNameContaining(String lastName);
		
		List<AuthorEntity> findByFirstNameAndLastNameContaining(String firstName,String lastName);
		
	
}
