package com.mitesh.security.RestSecurity.publisher;

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
import com.mitesh.security.RestSecurity.utils.PublisherUtils;

@Service
public class PublisherService {

	private static Logger logger=LoggerFactory.getLogger(PublisherService.class);
	
	
	private PublisherRepository publisherRepository;
	
	
	public PublisherService(PublisherRepository publisherRepository) {
		this.publisherRepository = publisherRepository;
	}


	public Publisher addPublisher(Publisher publisherToBeAdded, String traceId) throws ResourceAlreadyExistsException{
		PublisherEntity publisherEntity=new PublisherEntity(
									publisherToBeAdded.getName(),
									publisherToBeAdded.getEmailId(),
									publisherToBeAdded.getPhoneNumber());
							
		PublisherEntity addedPublisher=null;
		
		try {
		addedPublisher=publisherRepository.save(publisherEntity);
		}catch (DataIntegrityViolationException ex) {
			throw new ResourceAlreadyExistsException(traceId," Publiser is already exists..");
		}
		
		publisherToBeAdded.setPublisherId(addedPublisher.getPublisherid());
		return publisherToBeAdded;
	}


	public Publisher getPublisher(Integer publisherId,String traceId) throws ResourceNotFoundException {
			Optional<PublisherEntity> publisherEntity=	publisherRepository.findById(publisherId);
			Publisher publisher=null;
			if(publisherEntity.isPresent()) {
				PublisherEntity pe=publisherEntity.get();
				publisher=createPublisherFromEntity(pe);
			}else {
				throw new ResourceNotFoundException(traceId, "Publisher Id "+publisherId+" not found");
			}
			
			return publisher;
	}


	private Publisher createPublisherFromEntity(PublisherEntity pe) {
		return new Publisher(pe.getPublisherid(),pe.getName(),pe.getEmailId(),pe.getPhoneNumber());				
	}


	public void updatePublisher(Publisher publisherToBeUpdated,String traceId) throws ResourceNotFoundException {

		Optional<PublisherEntity> publisherEntity=	publisherRepository.findById(publisherToBeUpdated.getPublisherId());
		Publisher publisher=null;
		if(publisherEntity.isPresent()) {
			PublisherEntity pe=publisherEntity.get();
			
			if(PublisherUtils.doesStringValueExists(publisherToBeUpdated.getEmailId())) {
				pe.setEmailId(publisherToBeUpdated.getEmailId());
			}

			if(PublisherUtils.doesStringValueExists(publisherToBeUpdated.getPhoneNumber())) {
				pe.setPhoneNumber(publisherToBeUpdated.getPhoneNumber());
			}
			
			if(PublisherUtils.doesStringValueExists(publisherToBeUpdated.getName())) {
				pe.setName(publisherToBeUpdated.getName());
			}
			publisherRepository.save(pe);
			publisherToBeUpdated=createPublisherFromEntity(pe);
		}else {
			throw new ResourceNotFoundException(traceId, "Publisher Id "+publisherToBeUpdated.getPublisherId()+" not found");
		}
	}


	public void deletePublisher(Integer publisherId,String traceId) throws ResourceNotFoundException {
		try {
		publisherRepository.deleteById(publisherId);
		}catch (EmptyResultDataAccessException e) {
			logger.error("Trace Id: {}, Publisher Id: {} Not Found",traceId,publisherId,e);
			throw new ResourceNotFoundException(traceId," Publisher Id "+publisherId+" not found ");
		}
	}


	public List<Publisher> searchPublisher(String name,String traceId) {

		List<PublisherEntity> publisherEntities=null;
		if(PublisherUtils.doesStringValueExists(name)) {
			publisherEntities=publisherRepository.findByNameContaining(name);
		}
		if(publisherEntities!=null && publisherEntities.size()>0) {
			return createPublisherForSearchResponse(publisherEntities);
		}else {
			return Collections.emptyList();
		}
	}


	private List<Publisher> createPublisherForSearchResponse(List<PublisherEntity> publisherEntities) {
		return publisherEntities.stream().map(pe->createPublisherFromEntity(pe)).collect(Collectors.toList());
		
	}

}
