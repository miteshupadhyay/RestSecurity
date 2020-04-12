package com.mitesh.security.RestSecurity.user;

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
@RequestMapping(path = "/v1/users")
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<?> addUser(@Valid @RequestBody User user,@RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
			throws ResourceAlreadyExistsException {

		logger.debug("Request to add User: {}", user);
		if (!LibraryUtils.doesStringValueExists(traceId)) {
			traceId = UUID.randomUUID().toString();
		}
		logger.debug("Added TraceId: {}", traceId);
		userService.addUser(user, traceId);

		logger.debug("Returning response for TraceId: {}", traceId);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	
	@GetMapping(path = "/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Integer userId,
                                     @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws ResourceNotFoundException, ResourceUnauthorizedException {

        if(!LibraryUtils.doesStringValueExists(traceId)) {
        	 traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}", traceId);
        return new ResponseEntity<>(userService.getUser(userId, traceId), HttpStatus.OK);
	
	}
	
	
	@PutMapping(path = "/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Integer userId,
                                             @Valid @RequestBody User user,
                                             @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws ResourceNotFoundException, ResourceUnauthorizedException {

        if(!LibraryUtils.doesStringValueExists(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}", traceId);
        user.setUserId(userId);
        userService.updateUser(user, traceId);
        logger.debug("Returning response for TraceId: {}", traceId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
	@DeleteMapping(path = "/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId,
                                        @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws ResourceNotFoundException, ResourceUnauthorizedException {

        if(!LibraryUtils.doesStringValueExists(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}", traceId);

        userService.deleteUser(userId, traceId);
        logger.debug("Returning response for TraceId: {}", traceId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
	
	@GetMapping(path = "/search")
    public ResponseEntity<?> searchUser(@RequestParam String firstName, @RequestParam String lastName,
                                             @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws ResourceBadRequestException {

        if(!LibraryUtils.doesStringValueExists(traceId)) {
            traceId = UUID.randomUUID().toString();
        }

        if(!LibraryUtils.doesStringValueExists(firstName) && !LibraryUtils.doesStringValueExists(lastName)) {
            logger.error("TraceId: {}, Please enter at least one search criteria to search Users!!", traceId);
            throw new ResourceBadRequestException(traceId, "Please enter a name to search User.");
        }
        logger.debug("Returning response for TraceId: {}", traceId);
        return new ResponseEntity<>(userService.searchUser(firstName, lastName, traceId), HttpStatus.OK);
    }
}
