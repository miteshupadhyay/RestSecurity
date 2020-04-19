package com.mitesh.security.RestSecurity.publisher;

import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.mitesh.security.RestSecurity.exception.ResourceUnauthorizedException;
import com.mitesh.security.RestSecurity.utils.LibraryUtils;

@RestController
@RequestMapping(path = "/v1/publishers")
public class PublisherController {
	private static Logger logger = LoggerFactory.getLogger(PublisherController.class);

	private PublisherService publisherService;

	public PublisherController(PublisherService publisherService) {
		this.publisherService = publisherService;
	}

	@GetMapping(path = "/{publisherId}")
	public ResponseEntity<?> getPublisher(@PathVariable Integer publisherId,
			@RequestHeader(value = "Trace-Id", defaultValue = "") String traceId) throws ResourceNotFoundException {

		if (!LibraryUtils.doesStringValueExists(traceId)) {
			traceId = UUID.randomUUID().toString();
		}

		return new ResponseEntity<>(publisherService.getPublisher(publisherId, traceId), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> addPublisher(@Valid @RequestBody Publisher publisher,
			@RequestHeader(value = "Trace-Id", defaultValue = "") String traceId,
			@RequestHeader(value = "Authorization") String bearerToken)
			throws ResourceAlreadyExistsException, ResourceUnauthorizedException {

		logger.debug("Request to add Publisher: {}", publisher);
		if (!LibraryUtils.doesStringValueExists(traceId)) {
			traceId = UUID.randomUUID().toString();
		}

		if (!LibraryUtils.isUserAdmin(bearerToken)) {
			logger.error(LibraryUtils.getUserIdFromClaim(bearerToken)
					+ " attempted to add a Publisher. Disallowed because user is not Admin");
			throw new ResourceUnauthorizedException(traceId, "User not allowed to Add a Publisher");
		}
		logger.debug("Added TraceId: {}", traceId);
		publisherService.addPublisher(publisher, traceId);

		logger.debug("Returning response for TraceId: {}", traceId);
		return new ResponseEntity<>(publisher, HttpStatus.CREATED);
	}

	@PutMapping(path = "/{publisherId}")
	public ResponseEntity<?> updatePublisher(@PathVariable Integer publisherId, @Valid @RequestBody Publisher publisher,
			@RequestHeader(value = "Trace-Id", defaultValue = "") String traceId,
			@RequestHeader(value = "Authorization") String bearerToken)
			throws ResourceNotFoundException, ResourceUnauthorizedException {

		if (!LibraryUtils.doesStringValueExists(traceId)) {
			traceId = UUID.randomUUID().toString();
		}

		if (!LibraryUtils.isUserAdmin(bearerToken)) {
			logger.error(LibraryUtils.getUserIdFromClaim(bearerToken)
					+ " attempted to update a Publisher. Disallowed because user is not Admin");
			throw new ResourceUnauthorizedException(traceId, "User not allowed to Add a Publisher");
		}
		logger.debug("Added TraceId: {}", traceId);

		publisher.setPublisherId(publisherId);
		publisherService.updatePublisher(publisher, traceId);

		logger.debug("Returning response for TraceId: {}", traceId);
		return new ResponseEntity<>(publisher, HttpStatus.OK);
	}

	@DeleteMapping(path = "/{publisherId}")
	public ResponseEntity<?> deletePublisher(@PathVariable Integer publisherId,
			@RequestHeader(value = "Trace-Id", defaultValue = "") String traceId,
			@RequestHeader(value = "Authorization") String bearerToken)
			throws ResourceNotFoundException, ResourceUnauthorizedException {

		if (!LibraryUtils.doesStringValueExists(traceId)) {
			traceId = UUID.randomUUID().toString();
		}

		if (!LibraryUtils.isUserAdmin(bearerToken)) {
			logger.error(LibraryUtils.getUserIdFromClaim(bearerToken)
					+ " attempted to delete a Publisher. Disallowed because user is not Admin");
			throw new ResourceUnauthorizedException(traceId, "User not allowed to Add a Publisher");
		}
		logger.debug("Added TraceId: {}", traceId);

		publisherService.deletePublisher(publisherId, traceId);
		logger.debug("Returning response for TraceId: {}", traceId);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@GetMapping(path = "/search")
	public ResponseEntity<?> searchPublisher(@RequestParam String name,
			@RequestHeader(value = "Trace-Id", defaultValue = "") String traceId) throws ResourceBadRequestException {

		if (!LibraryUtils.doesStringValueExists(traceId)) {
			traceId = UUID.randomUUID().toString();
		}

		if (!LibraryUtils.doesStringValueExists(name)) {
			logger.error("TraceId: {}, Please enter a name to search Publisher!!", traceId);
			throw new ResourceBadRequestException(traceId, "Please enter a name to search Publisher.");
		}
		logger.debug("Returning response for TraceId: {}", traceId);
		return new ResponseEntity<>(publisherService.searchPublisher(name, traceId), HttpStatus.OK);
	}
}
