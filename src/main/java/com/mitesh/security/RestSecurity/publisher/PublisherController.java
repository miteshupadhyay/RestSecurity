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
			@RequestHeader(value = "Trace-Id",defaultValue = "")String traceId) {
		if(!PublisherUtils.doesStringValueExists(traceId)) {
			traceId=UUID.randomUUID().toString();
		}
		Publisher publisher=null;
		try {
			publisher=publisherService.getPublisher(publisherId,traceId);
		}catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(publisher,HttpStatus.OK);
	}

	@PutMapping(path = "/{publisherId}")
	public ResponseEntity<?> updatePublisher(@PathVariable Integer publisherId,@RequestBody Publisher publisher,
			@RequestHeader(value = "Trace-Id",defaultValue = "")String traceId) {
		if(!PublisherUtils.doesStringValueExists(traceId)) {
			traceId=UUID.randomUUID().toString();
		}
		try {
			publisher.setPublisherId(publisherId);
			publisherService.updatePublisher(publisher,traceId);
		}catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(publisher,HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> addPublisher(@Valid @RequestBody Publisher publisher,
			@RequestHeader(value = "Trace-Id",defaultValue = "")String traceId) {
		
		if(!PublisherUtils.doesStringValueExists(traceId)) {
			traceId=UUID.randomUUID().toString();
		}
		
		try {
			publisher=publisherService.addPublisher(publisher,traceId);
		} catch (ResourceAlreadyExistsException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(publisher,HttpStatus.CREATED);
	}
	@DeleteMapping(path = "/{publisherId}")
	public ResponseEntity<?> deletePublisher(@PathVariable Integer publisherId,
			@RequestHeader(value = "Trace-Id",defaultValue = "")String traceId) {
		
		if(!PublisherUtils.doesStringValueExists(traceId)) {
			traceId=UUID.randomUUID().toString();
		}
		Publisher publisher=null;
		try {
			publisherService.deletePublisher(publisherId,traceId);
		}catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(publisher,HttpStatus.ACCEPTED);
	}
	@GetMapping(path = "/search")
	public ResponseEntity<?> getPublisher(@RequestParam String name,
			@RequestHeader(value = "Trace-Id",defaultValue = "")String traceId) {
		
		if(!PublisherUtils.doesStringValueExists(traceId)) {
			traceId=UUID.randomUUID().toString();
		}
		if(!PublisherUtils.doesStringValueExists(name)) {
			return new ResponseEntity<>("Please enter a name to search publisher ",HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(publisherService.searchPublisher(name,traceId),HttpStatus.OK);
	}
}
