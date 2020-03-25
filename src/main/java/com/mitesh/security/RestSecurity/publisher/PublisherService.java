package com.mitesh.security.RestSecurity.publisher;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.mitesh.security.RestSecurity.exception.ResourceAlreadyExistsException;

@Service
public class PublisherService {

	private PublisherRepository publisherRepository;
	
	
	public PublisherService(PublisherRepository publisherRepository) {
		this.publisherRepository = publisherRepository;
	}


	public Publisher addPublisher(Publisher publisherToBeAdded) throws ResourceAlreadyExistsException{
		PublisherEntity publisherEntity=new PublisherEntity(
									publisherToBeAdded.getName(),
									publisherToBeAdded.getEmailId(),
									publisherToBeAdded.getPhoneNumber());
							
		PublisherEntity addedPublisher=null;
		
		try {
		addedPublisher=publisherRepository.save(publisherEntity);
		}catch (DataIntegrityViolationException ex) {
			throw new ResourceAlreadyExistsException("Publiser is already exists..");
		}
		
		publisherToBeAdded.setPublisherId(addedPublisher.getPublisherid());
		return publisherToBeAdded;
	}

}
