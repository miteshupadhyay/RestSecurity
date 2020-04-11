package com.mitesh.security.RestSecurity.author;
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
@RequestMapping(path = "/v1/authors")
public class AuthorController {

    private static Logger logger = LoggerFactory.getLogger(AuthorController.class);

    private AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(path = "/{authorId}")
    public ResponseEntity<?> getAuthor(@PathVariable Integer authorId,
                                          @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws ResourceNotFoundException {

        if(!LibraryUtils.doesStringValueExists(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}", traceId);
        return new ResponseEntity<>(authorService.getAuthor(authorId, traceId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addAuthor(@Valid @RequestBody Author author,
                                          @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId,
                                          @RequestHeader(value = "Authorization") String bearerToken)
            throws ResourceAlreadyExistsException, ResourceUnauthorizedException {

        logger.debug("Request to add Author: {}", author);
        if(!LibraryUtils.doesStringValueExists(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}", traceId);
        if(!LibraryUtils.isUserAdmin(bearerToken)) {
            logger.error(LibraryUtils.getUserIdFromClaim(bearerToken) + " attempted to add a Author. Disallowed because user is not Admin");
            throw new ResourceUnauthorizedException(traceId, "User not allowed to Add a Author");
        }
        authorService.addAuthor(author, traceId);

        logger.debug("Returning response for TraceId: {}", traceId);
        return new ResponseEntity<>(author, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{authorId}")
    public ResponseEntity<?> updateAuthor(@PathVariable Integer authorId,
                                          @Valid @RequestBody Author author,
                                          @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId,
                                          @RequestHeader(value = "Authorization") String bearerToken)
            throws ResourceNotFoundException, ResourceUnauthorizedException {

        if(!LibraryUtils.doesStringValueExists(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}", traceId);
        if(!LibraryUtils.isUserAdmin(bearerToken)) {
            logger.error(LibraryUtils.getUserIdFromClaim(bearerToken) + " attempted to update a Author. Disallowed because user is not Admin");
            throw new ResourceUnauthorizedException(traceId, "User not allowed to Add a Author");
        }

        author.setAuthorId(authorId);
        authorService.updateAuthor(author, traceId);
        logger.debug("Returning response for TraceId: {}", traceId);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{authorId}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Integer authorId,
                                          @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId,
                                          @RequestHeader(value = "Authorization") String bearerToken)
            throws ResourceNotFoundException, ResourceUnauthorizedException {

        if(!LibraryUtils.doesStringValueExists(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        if(!LibraryUtils.isUserAdmin(bearerToken)) {
            logger.error(LibraryUtils.getUserIdFromClaim(bearerToken) + " attempted to delete a Author. Disallowed because user is not Admin");
            throw new ResourceUnauthorizedException(traceId, "User not allowed to Add a Author");
        }
        logger.debug("Added TraceId: {}", traceId);
        authorService.deleteAuthor(authorId, traceId);
        logger.debug("Returning response for TraceId: {}", traceId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<?> searchAuthor(@RequestParam String firstName, @RequestParam String lastName,
                                          @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws ResourceBadRequestException
    {

        if(!LibraryUtils.doesStringValueExists(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}", traceId);
        if(!LibraryUtils.doesStringValueExists(firstName) && !LibraryUtils.doesStringValueExists(lastName)) {
            logger.error("TraceId: {}, Please enter at least one search criteria to search Authors!!", traceId);
            throw new ResourceBadRequestException(traceId, "Please enter a name to search Author.");
        }
        logger.debug("Returning response for TraceId: {}", traceId);
        return new ResponseEntity<>(authorService.searchAuthor(firstName, lastName, traceId), HttpStatus.OK);
    }
}