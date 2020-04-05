package com.mitesh.security.RestSecurity.publisher;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.mitesh.security.RestSecurity.exception.ResourceAlreadyExistsException;
import com.mitesh.security.RestSecurity.exception.ResourceNotFoundException;

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


	public Publisher getPublisher(Integer publisherId) throws ResourceNotFoundException {
			Optional<PublisherEntity> publisherEntity=	publisherRepository.findById(publisherId);
			Publisher publisher=null;
			if(publisherEntity.isPresent()) {
				PublisherEntity pe=publisherEntity.get();
				publisher=createPublisherFromEntity(pe);
			}else {
				throw new ResourceNotFoundException("Publisher Id "+publisherId+" not found");
			}
			
			return publisher;
	}


	private Publisher createPublisherFromEntity(PublisherEntity pe) {
		return new Publisher(pe.getPublisherid(),pe.getName(),pe.getEmailId(),pe.getPhoneNumber());				
	}

}
