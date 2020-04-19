package com.mitesh.security.RestSecurity.exception;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mitesh.security.RestSecurity.model.common.LibraryApiError;
import com.mitesh.security.RestSecurity.utils.LibraryUtils;

@ControllerAdvice
public class PublisherControllerExceptionHandler extends ResponseEntityExceptionHandler
{
	private static Logger logger = LoggerFactory.getLogger(PublisherControllerExceptionHandler.class);

@ExceptionHandler(ResourceNotFoundException.class)
public final ResponseEntity<LibraryApiError> handleLibraryResourceNotFoundException(
		ResourceNotFoundException e, WebRequest webRequest) {

    return new ResponseEntity<>(new LibraryApiError(e.getTraceId(), e.getMessage()), HttpStatus.NOT_FOUND);
}

@ExceptionHandler(ResourceAlreadyExistsException.class)
public final ResponseEntity<LibraryApiError> handleLibraryResourceAlreadyExistException(
		ResourceAlreadyExistsException e, WebRequest webRequest) {

    return new ResponseEntity<>(new LibraryApiError(e.getTraceId(), e.getMessage()), HttpStatus.CONFLICT);
}

@ExceptionHandler(ResourceBadRequestException.class)
public final ResponseEntity<LibraryApiError> handleLibraryResourceBadRequestException(
		ResourceBadRequestException e, WebRequest webRequest) {

    return new ResponseEntity<>(new LibraryApiError(e.getTraceId(), e.getMessage()), HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(ResourceUnauthorizedException.class)
public final ResponseEntity<LibraryApiError> handleLibraryResourceUnauthorizedException(
		ResourceUnauthorizedException e, WebRequest webRequest) {

    return new ResponseEntity<>(new LibraryApiError(e.getTraceId(), e.getMessage()), HttpStatus.FORBIDDEN);
}

@ExceptionHandler(Exception.class)
public final ResponseEntity<LibraryApiError> handleAllException(
        Exception e, WebRequest webRequest) {

    String traceId = getTraceId(webRequest);
    logger.error(traceId, e);
    return new ResponseEntity<>(new LibraryApiError(traceId, e.getMessage()), HttpStatus.BAD_REQUEST);
}

private String getTraceId(WebRequest webRequest) {
    String traceId = webRequest.getHeader("Trace-Id");
    if(!LibraryUtils.doesStringValueExists(traceId)) {
        traceId = UUID.randomUUID().toString();
    }

    return traceId;
}
}
