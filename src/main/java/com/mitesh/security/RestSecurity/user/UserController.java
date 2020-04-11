package com.mitesh.security.RestSecurity.user;
import java.util.Set;
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

    @GetMapping(path = "/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Integer userId,
                                     @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId,
                                     @RequestHeader(value = "Authorization") String bearerToken)
            throws ResourceNotFoundException,ResourceUnauthorizedException {

        if(!LibraryUtils.doesStringValueExists(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}", traceId);

        if(LibraryUtils.isUserAdmin(bearerToken)) {
            logger.error("Trace Id: {}, even an admin user is not allowed to get a user's details", traceId);
            throw new ResourceUnauthorizedException(traceId, "Even an admin user is not allowed to get a user's details");
        }

        int userIdInClaim = LibraryUtils.getUserIdFromClaim(bearerToken);
        if(userIdInClaim != userId) {
            logger.error("Trace Id: {}, UserId {} not allowed to get the details of another user {} ", traceId, userIdInClaim, userId);
            throw new ResourceUnauthorizedException(traceId, "Not allowed to get the details of another user");
        }

        return new ResponseEntity<>(userService.getUser(userId, traceId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody User user,
                                          @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws ResourceAlreadyExistsException {

        logger.debug("Request to add User: {}", user);
        if(!LibraryUtils.doesStringValueExists(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}", traceId);
        userService.addUser(user, traceId);

        logger.debug("Returning response for TraceId: {}", traceId);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Integer userId,
                                             @Valid @RequestBody User user,
                                             @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId,
                                            @RequestHeader(value = "Authorization") String bearerToken)
            throws ResourceNotFoundException, ResourceUnauthorizedException {

        if(!LibraryUtils.doesStringValueExists(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}", traceId);
        if(LibraryUtils.isUserAdmin(bearerToken)) {
            logger.error("Trace Id: {}, even an admin user is not allowed to update a user's details", traceId);
            throw new ResourceUnauthorizedException(traceId, "Even an admin user is not allowed to update a user's details");
        }

        int userIdInClaim = LibraryUtils.getUserIdFromClaim(bearerToken);
        if(userIdInClaim != userId) {
            logger.error("Trace Id: {}, UserId {} not allowed to update the details of another user {} ", traceId, userIdInClaim, userId);
            throw new ResourceUnauthorizedException(traceId, "Not allowed to update the details of another user");
        }
        user.setUserId(userId);
        userService.updateUser(user, traceId);
        logger.debug("Returning response for TraceId: {}", traceId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId,
                                        @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId,
                                        @RequestHeader(value = "Authorization") String bearerToken)
            throws ResourceNotFoundException, ResourceUnauthorizedException {

        if(!LibraryUtils.doesStringValueExists(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}", traceId);

        if(LibraryUtils.isUserAdmin(bearerToken)) {
            logger.error("Trace Id: {}, even an admin user is not allowed to delete a user", traceId);
            throw new ResourceUnauthorizedException(traceId, "Even an admin user is not allowed to delete a user");
        }

        int userIdInClaim = LibraryUtils.getUserIdFromClaim(bearerToken);
        if(userIdInClaim != userId) {
            logger.error("Trace Id: {}, UserId {} not allowed to delete another user {} ", traceId, userIdInClaim, userId);
            throw new ResourceUnauthorizedException(traceId, "Not allowed to delete another user");
        }

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

    @PutMapping(path = "/{userId}/books")
    public ResponseEntity<?> issueBooks(@PathVariable int userId, @RequestBody Set<Integer> bookIds,
                                        @RequestHeader("Authorization") String bearerToken,
                                        @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws ResourceUnauthorizedException, ResourceBadRequestException, ResourceNotFoundException {
        if(!LibraryUtils.doesStringValueExists(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        if(!LibraryUtils.isUserAdmin(bearerToken)) {
            // Logging UserId for security audit trail.
            logger.error(traceId +  LibraryUtils.getUserIdFromClaim(bearerToken) + " attempted to issue Books. Disallowed. " +
                    "User is not a Admin.");
            throw new ResourceUnauthorizedException(traceId, " attempted to issue Books. Disallowed.");
        }
        if(bookIds == null || bookIds.size() == 0) {
            logger.error(traceId + " Invalid Book list. List is either not present or empty.");
            throw new ResourceBadRequestException(traceId, "Invalid Book list. List is either not present or empty.");
        }
        IssueBookResponse issueBookResponse = null;
        try {
            issueBookResponse = userService.issueBooks(userId, bookIds, traceId);
        } catch (ResourceNotFoundException e) {
            logger.error(traceId + e.getMessage());
            throw e;
        }
        return new ResponseEntity<>(issueBookResponse, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userId}/books/{bookId}")
    public ResponseEntity<?> returnBooks(@PathVariable int userId, @PathVariable int bookId,
                                         @RequestHeader("Authorization") String bearerToken,
                                         @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws ResourceUnauthorizedException, ResourceBadRequestException, ResourceNotFoundException {
        if(!LibraryUtils.doesStringValueExists(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        if(!LibraryUtils.isUserAdmin(bearerToken)) {
            // Logging UserId for security audit trail.
            logger.error(traceId +  LibraryUtils.getUserIdFromClaim(bearerToken) + " attempted to return Books. Disallowed. " +
                    "User is not a Admin.");
            throw new ResourceUnauthorizedException(traceId, " attempted to delete Books. Disallowed.");
        }
        try {
            userService.returnBooks(userId, bookId, traceId);
        } catch (ResourceNotFoundException e) {
            logger.error(traceId + e.getMessage());
            throw e;
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}