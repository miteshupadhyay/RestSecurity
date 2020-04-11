package com.mitesh.security.RestSecurity.publisher;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mitesh.security.RestSecurity.exception.ResourceAlreadyExistsException;
import com.mitesh.security.RestSecurity.exception.ResourceBadRequestException;
import com.mitesh.security.RestSecurity.exception.ResourceNotFoundException;
import com.mitesh.security.RestSecurity.utils.PublisherUtils;

@RestController
@RequestMapping(path = "/v1/publishers")
public class PublisherController {

	private PublisherService publisherService;
	
	public PublisherController(PublisherService publisherService) {
		this.publisherService = publisherService;
	}

	@GetMapping(path = "/{publisherId}")
	public ResponseEntity<?> getPublisher(@PathVariable Integer publisherId,
			@RequestHeader(value = "Trace-Id",defaultValue = "")String traceId) throws ResourceNotFoundException {
		if(!PublisherUtils.doesStringValueExists(traceId)) {
			traceId=UUID.randomUUID().toString();
		}
		Publisher publisher=null;
			publisher=publisherService.getPublisher(publisherId,traceId);
		
		return new ResponseEntity<>(publisher,HttpStatus.OK);
	}

	@PutMapping(path = "/{publisherId}")
	public ResponseEntity<?> updatePublisher(@PathVariable Integer publisherId,@RequestBody Publisher publisher,
			@RequestHeader(value = "Trace-Id",defaultValue = "")String traceId) throws ResourceNotFoundException {
		if(!PublisherUtils.doesStringValueExists(traceId)) {
			traceId=UUID.randomUUID().toString();
		}
			publisher.setPublisherId(publisherId);
			publisherService.updatePublisher(publisher,traceId);
		
		return new ResponseEntity<>(publisher,HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> addPublisher(@Valid @RequestBody Publisher publisher,
			@RequestHeader(value = "Trace-Id",defaultValue = "")String traceId) throws ResourceAlreadyExistsException {
		
		if(!PublisherUtils.doesStringValueExists(traceId)) {
			traceId=UUID.randomUUID().toString();
		}
		
		publisher=publisherService.addPublisher(publisher,traceId);
		
		return new ResponseEntity<>(publisher,HttpStatus.CREATED);
	}
	@DeleteMapping(path = "/{publisherId}")
	public ResponseEntity<?> deletePublisher(@PathVariable Integer publisherId,
			@RequestHeader(value = "Trace-Id",defaultValue = "")String traceId) throws ResourceNotFoundException {
		
		if(!PublisherUtils.doesStringValueExists(traceId)) {
			traceId=UUID.randomUUID().toString();
		}
		Publisher publisher=null;
		publisherService.deletePublisher(publisherId,traceId);
		return new ResponseEntity<>(publisher,HttpStatus.ACCEPTED);
	}
	@GetMapping(path = "/search")
	public ResponseEntity<?> getPublisher(@RequestParam String name,
			@RequestHeader(value = "Trace-Id",defaultValue = "")String traceId) throws ResourceBadRequestException {
		
		if(!PublisherUtils.doesStringValueExists(traceId)) {
			traceId=UUID.randomUUID().toString();
		}
		if(!PublisherUtils.doesStringValueExists(name)) {
			throw new ResourceBadRequestException(traceId, "Please enter a name to search publisher ");
		}
		return new ResponseEntity<>(publisherService.searchPublisher(name,traceId),HttpStatus.OK);
	}
	
	
	
	
}
