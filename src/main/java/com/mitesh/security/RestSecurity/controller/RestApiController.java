package com.mitesh.security.RestSecurity.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitesh.security.RestSecurity.model.Publisher;

@RestController
@RequestMapping(path = "/v1/publishers")
public class RestApiController {

	@GetMapping(path = "/{publisherId}")
	public Publisher getPublisher(@PathVariable String publisherId) {
		return new Publisher(publisherId, "Mitesh","mitesh@gmail.com", "12346789");
	}
}
