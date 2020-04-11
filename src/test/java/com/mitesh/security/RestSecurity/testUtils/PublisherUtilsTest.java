package com.mitesh.security.RestSecurity.testUtils;

import java.util.Optional;

import com.mitesh.security.RestSecurity.publisher.Publisher;
import com.mitesh.security.RestSecurity.publisher.PublisherEntity;

public class PublisherUtilsTest {

	public static Publisher createPublisher() {
		return new Publisher(null,TestConstants.TEST_PUBLISHER_NAME,
				TestConstants.TEST_PUBLISHER_EMAIL,
				TestConstants.TEST_PUBLISHER_PHONE);
	}
	
	public static PublisherEntity createPublisherEntity() {
		return new PublisherEntity(TestConstants.TEST_PUBLISHER_NAME,
				TestConstants.TEST_PUBLISHER_EMAIL,
				TestConstants.TEST_PUBLISHER_PHONE);
	}

	public static Optional<PublisherEntity> createPublisherEntityOptional() {
		return Optional.of(createPublisherEntity());
		 
	}
	
}
