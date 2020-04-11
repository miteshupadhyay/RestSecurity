package com.mitesh.security.RestSecurity.publisher;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.mitesh.security.RestSecurity.exception.ResourceAlreadyExistsException;
import com.mitesh.security.RestSecurity.exception.ResourceNotFoundException;
import com.mitesh.security.RestSecurity.testUtils.PublisherUtilsTest;
import com.mitesh.security.RestSecurity.testUtils.TestConstants;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class PublisherServiceTest {

	@Mock
	PublisherRepository publisherRepository;
	
	PublisherService publisherService;
	
	
	@Before
	public void setUp() throws Exception {
		publisherService=new PublisherService(publisherRepository);
	}

	@Test
	public void testPublisherService() {
		fail("Not yet implemented");
	}

	@Test
	public void addPublisher_success() throws ResourceAlreadyExistsException {
		when(publisherRepository.save(any(PublisherEntity.class))).thenReturn(PublisherUtilsTest.createPublisherEntity());
		
		Publisher publisher=PublisherUtilsTest.createPublisher();
		publisherService.addPublisher(publisher, TestConstants.API_TRACE_ID);
		
		verify(publisherRepository,times(1)).save(any(PublisherEntity.class));
		assertNotNull(publisher.getPublisherId());
		assertTrue(publisher.getName().equals(TestConstants.TEST_PUBLISHER_NAME));
		
	}
	
	@Test(expected =ResourceAlreadyExistsException.class)
	public void addPublisher_failure() throws ResourceAlreadyExistsException {
		
		doThrow(DataIntegrityViolationException.class).when(publisherRepository).save(any(PublisherEntity.class));
		Publisher publisher=PublisherUtilsTest.createPublisher();
		publisherService.addPublisher(publisher, TestConstants.API_TRACE_ID);		
		verify(publisherRepository,times(1)).save(any(PublisherEntity.class));
		
	}

	@Test
	public void getPublisher_success() throws ResourceNotFoundException {
		
		when(publisherRepository.findById(anyInt())).thenReturn(PublisherUtilsTest.createPublisherEntityOptional());
		
		Publisher publisher = publisherService.getPublisher(123, TestConstants.API_TRACE_ID);
		verify(publisherRepository, times(1)).findById(123);
		assertNotNull(publisher.getPublisherId());
		
	}

	@Test(expected = ResourceNotFoundException.class)
	public void getPublisher_failure() throws ResourceNotFoundException {		
		when(publisherRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		publisherService.getPublisher(123, TestConstants.API_TRACE_ID);
		verify(publisherRepository, times(1)).findById(123);		
	}
	
	
	@Test
	public void updatePublisher_success() throws ResourceAlreadyExistsException, ResourceNotFoundException {
		PublisherEntity publisherEntity = PublisherUtilsTest.createPublisherEntity();
		
		when(publisherRepository.save(any(PublisherEntity.class))).thenReturn(publisherEntity);
	
		Publisher publisher=PublisherUtilsTest.createPublisher();
		publisherService.addPublisher(publisher, TestConstants.API_TRACE_ID);
		verify(publisherRepository,times(1)).save(any(PublisherEntity.class));
		
		publisher.setEmailId(TestConstants.TEST_PUBLISHER_EMAIL_UPDATED);
		publisher.setPhoneNumber(TestConstants.TEST_PUBLISHER_PHONE_UPDATED);
		
		when(publisherRepository.findById(anyInt())).thenReturn(PublisherUtilsTest.createPublisherEntityOptional());
		publisherService.updatePublisher(publisher, TestConstants.API_TRACE_ID);
		
		verify(publisherRepository,times(1)).findById(publisher.getPublisherId());
		verify(publisherRepository,times(2)).save(any(PublisherEntity.class));
		
		assertTrue(TestConstants.TEST_PUBLISHER_EMAIL_UPDATED.equals(publisher.getEmailId()));		
	}

	@Test
	public void testDeletePublisher_success() throws ResourceNotFoundException {
		
		doNothing().when(publisherRepository).deleteById(anyInt());
		publisherService.deletePublisher(anyInt(), TestConstants.API_TRACE_ID);
		
		verify(publisherRepository,times(1)).deleteById(anyInt());
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void testDeletePublisher_failure() throws ResourceNotFoundException {
		
		doThrow(EmptyResultDataAccessException.class).when(publisherRepository).deleteById(anyInt());
		
		publisherService.deletePublisher(anyInt(), TestConstants.API_TRACE_ID);
		verify(publisherRepository,times(1)).deleteById(anyInt());
	}

	@Test
	public void searchPublisher_success() {
		
		List<PublisherEntity> publisherEntitiesList=Arrays.asList(
				new PublisherEntity(TestConstants.TEST_PUBLISHER_NAME+1, TestConstants.TEST_PUBLISHER_EMAIL, TestConstants.TEST_PUBLISHER_PHONE),
				new PublisherEntity(TestConstants.TEST_PUBLISHER_NAME+2, TestConstants.TEST_PUBLISHER_EMAIL, TestConstants.TEST_PUBLISHER_PHONE));
		
		when(publisherRepository.findByNameContaining(TestConstants.TEST_PUBLISHER_NAME)).thenReturn(publisherEntitiesList);
		List<Publisher> searchPublisher = publisherService.searchPublisher(TestConstants.TEST_PUBLISHER_NAME, TestConstants.API_TRACE_ID);
		
		verify(publisherRepository,times(1)).findByNameContaining(TestConstants.TEST_PUBLISHER_NAME);
		assertEquals(publisherEntitiesList.size(), searchPublisher.size());
		
		assertEquals(publisherEntitiesList.size(), searchPublisher.stream()
					.filter(publisher->publisher.getName().contains(TestConstants.TEST_PUBLISHER_NAME)).count());
		
	}

	@Test
	public void searchPublisher_failure() {
		
		when(publisherRepository.findByNameContaining(TestConstants.TEST_PUBLISHER_NAME)).thenReturn(Collections.emptyList());
		List<Publisher> searchPublisher = publisherService.searchPublisher(TestConstants.TEST_PUBLISHER_NAME, TestConstants.API_TRACE_ID);
		
		verify(publisherRepository,times(1)).findByNameContaining(TestConstants.TEST_PUBLISHER_NAME);
		assertEquals(0, searchPublisher.size());
	}
}
