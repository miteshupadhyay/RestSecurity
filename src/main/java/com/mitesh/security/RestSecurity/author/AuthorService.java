package com.mitesh.security.RestSecurity.author;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.mitesh.security.RestSecurity.exception.ResourceAlreadyExistsException;
import com.mitesh.security.RestSecurity.exception.ResourceNotFoundException;
import com.mitesh.security.RestSecurity.utils.LibraryUtils;

@Service
public class AuthorService {

	private static Logger logger=LoggerFactory.getLogger(AuthorService.class);
	
	private AuthorRepository authorRepository;
	
	public AuthorService(AuthorRepository authorRepository) {
		this.authorRepository=authorRepository;
	}
	
	public void addAuthor(Author authorToBeAdded,String traceId) throws ResourceAlreadyExistsException{
		logger.debug("TraceId: {}, Request to add author: {}",traceId,authorToBeAdded);
		
		AuthorEntity authorEntity=new AuthorEntity(
					authorToBeAdded.getFirstName(),
					authorToBeAdded.getLastName(),
					authorToBeAdded.getDateOfBirth(),
					authorToBeAdded.getGender());
		
		AuthorEntity addedAuthor=null;
		
		try {
			addedAuthor=authorRepository.save(authorEntity);
		}catch (DataIntegrityViolationException e) {
			logger.error("TraceId: {}, Author Already Exists !! ",traceId,e);
			throw new ResourceAlreadyExistsException(traceId," Author is already exists..");
		}		
		authorToBeAdded.setAuthorId(addedAuthor.getAuthorId());
		logger.error("TraceId: {}, Author Added Successfully !! ",traceId,addedAuthor);
		
	}

	public Author getAuthor(Integer authorId, String traceId) throws ResourceNotFoundException {
			
			Optional<AuthorEntity> authorEntity = authorRepository.findById(authorId);
			
			Author author=null;
			if(authorEntity.isPresent()) {
				AuthorEntity ae=authorEntity.get();
				author=createAuthorFromEntity(ae);
			}else {
				throw new ResourceNotFoundException(traceId,"Author Id "+authorId +" not found");
			}
		return author;
	}

	private Author createAuthorFromEntity(AuthorEntity ae) {
		return new Author(ae.getAuthorId(), ae.getFirstName(), ae.getLastName(),ae.getDateOfBirth(),ae.getGender());
	}

	public void updateAuthor(Author authorToBeUpdated, String traceId) throws ResourceNotFoundException{

			Optional<AuthorEntity> authorEntity=authorRepository.findById(authorToBeUpdated.getAuthorId());
			if(authorEntity.isPresent()) {
				AuthorEntity ae=authorEntity.get();
				if(authorToBeUpdated.getDateOfBirth()!=null) {
					ae.setDateOfBirth(authorToBeUpdated.getDateOfBirth());
				}
				if(authorToBeUpdated.getGender()!=null) {
					ae.setGender(authorToBeUpdated.getGender());
				}
				
				authorRepository.save(ae);
				
				authorToBeUpdated=createAuthorFromEntity(ae);
			}else {
				throw new ResourceNotFoundException(traceId,"Author Id "+authorToBeUpdated.getAuthorId()+" not found");
			}	
	}

	public void deleteAuthor(Integer authorId, String traceId) throws ResourceNotFoundException {

		try {
			authorRepository.deleteById(authorId);
		}catch (EmptyResultDataAccessException e) {
			logger.error("TraceId: {}, Author Id: {} Not Found", traceId, authorId, e);
			throw new ResourceNotFoundException(traceId, "Author Id "+authorId+" not found");
		}
	}

	public List<Author> searchAuthor(String firstName, String lastName, String traceId) {
		
		List<AuthorEntity> authorEntities = null;
        if(LibraryUtils.doesStringValueExists(firstName) && LibraryUtils.doesStringValueExists(lastName)) {
            authorEntities = authorRepository.findByFirstNameAndLastNameContaining(firstName, lastName);
        } else if(LibraryUtils.doesStringValueExists(firstName) && !LibraryUtils.doesStringValueExists(lastName)) {
            authorEntities = authorRepository.findByFirstNameContaining(firstName);
        } else if(!LibraryUtils.doesStringValueExists(firstName) && LibraryUtils.doesStringValueExists(lastName)) {
            authorEntities = authorRepository.findByLastNameContaining(lastName);
        }
		
        if(authorEntities != null && authorEntities.size() > 0) {
            return createAuthorsForSearchResponse(authorEntities);
        } else {
            return Collections.emptyList();
        }
	}

	private List<Author> createAuthorsForSearchResponse(List<AuthorEntity> authorEntities) {
		 return authorEntities.stream()
	                .map(ae -> new Author(ae.getAuthorId(), ae.getFirstName(), ae.getLastName(), ae.getDateOfBirth(), ae.getGender()))
	                .collect(Collectors.toList());			
	}
}
