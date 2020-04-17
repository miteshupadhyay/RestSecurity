package com.mitesh.security.RestSecurity.user;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mitesh.security.RestSecurity.exception.ResourceAlreadyExistsException;
import com.mitesh.security.RestSecurity.exception.ResourceNotFoundException;
import com.mitesh.security.RestSecurity.security.SecurityConstants;
import com.mitesh.security.RestSecurity.utils.LibraryUtils;

@Service
public class UserService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class);
	
	private UserRepository userRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserService(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder=bCryptPasswordEncoder;
	}

	public void addUser(User userToBeAdded,String traceId) throws ResourceAlreadyExistsException{
		
		logger.debug("Trace Id: {}, Request to Add user: {}",traceId,userToBeAdded);
		
		UserEntity userEntity=new UserEntity(
											userToBeAdded.getUsername(),				
											bCryptPasswordEncoder.encode(SecurityConstants.NEW_USER_DEFAULT_PASSWORD),
											userToBeAdded.getFirstName(),
											userToBeAdded.getLastName(), 
											userToBeAdded.getDateOfBirth(),
											userToBeAdded.getGender(),
											userToBeAdded.getPhoneNumber(),
											userToBeAdded.getEmailId(),
											"USER");
		
			userToBeAdded.setPassword(SecurityConstants.NEW_USER_DEFAULT_PASSWORD);
			
			UserEntity addedUser=null;
			try {
				addedUser=userRepository.save(userEntity);
			}catch (DataIntegrityViolationException e) {
				 logger.error("TraceId: {}, User already exists!!", traceId, e);
				 throw new ResourceAlreadyExistsException(traceId,"User Already Exists ..!!!");
			}
			
			userToBeAdded.setUserId(addedUser.getUserId());
			userToBeAdded.setRole(Role.USER);
			
			logger.debug("Trace Id: {},User Added  Successfully: {} ",traceId,userToBeAdded);			
	}
	
	public User getUser(Integer userId, String traceId) throws ResourceNotFoundException {

        Optional<UserEntity> userEntity = userRepository.findById(userId);
        User user = null;

        if(userEntity.isPresent()) {
            UserEntity pe = userEntity.get();
            user = createUserFromEntity(pe);
        } else {
            throw new ResourceNotFoundException(traceId, "User Id: " + userId + " Not Found");
        }

        return user;
    }
	 public void updateUser(User userToBeUpdated, String traceId) throws ResourceNotFoundException {

	        Optional<UserEntity> userEntity = userRepository.findById(userToBeUpdated.getUserId());

	        if(userEntity.isPresent()) {
	            UserEntity ue = userEntity.get();
	            if(LibraryUtils.doesStringValueExists(userToBeUpdated.getEmailId())) {
	                ue.setEmailId(userToBeUpdated.getEmailId());
	            }
	            if(LibraryUtils.doesStringValueExists(userToBeUpdated.getPhoneNumber())) {
	                ue.setPhoneNumber(userToBeUpdated.getPhoneNumber());
	            }
	            if(LibraryUtils.doesStringValueExists(userToBeUpdated.getPassword())) {
	                ue.setPassword(userToBeUpdated.getPassword());
	            }
	            userRepository.save(ue);
	            userToBeUpdated = createUserFromEntity(ue);

	        } else {
	            throw new ResourceNotFoundException(traceId, "User Id: " + userToBeUpdated.getUserId() + " Not Found");
	        }

	    }
	 public void deleteUser(Integer userId, String traceId) throws ResourceNotFoundException {

	        try {
	            userRepository.deleteById(userId);
	        } catch(EmptyResultDataAccessException e) {
	            logger.error("TraceId: {}, User Id: {} Not Found", traceId, userId, e);
	            throw new ResourceNotFoundException(traceId, "User Id: " + userId + " Not Found");
	        }
	    }
	 public List<User> searchUser(String firstName, String lastName, String traceId) {

	        List<UserEntity> userEntities = null;
	        if(LibraryUtils.doesStringValueExists(firstName) && LibraryUtils.doesStringValueExists(lastName)) {
	            userEntities = userRepository.findByFirstNameAndLastNameContaining(firstName, lastName);
	        } else if(LibraryUtils.doesStringValueExists(firstName) && !LibraryUtils.doesStringValueExists(lastName)) {
	            userEntities = userRepository.findByFirstNameContaining(firstName);
	        } else if(!LibraryUtils.doesStringValueExists(firstName) && LibraryUtils.doesStringValueExists(lastName)) {
	            userEntities = userRepository.findByLastNameContaining(lastName);
	        }
	        if(userEntities != null && userEntities.size() > 0) {
	            return createUsersForSearchResponse(userEntities);
	        } else {
	            return Collections.emptyList();
	        }
	    }
	 private List<User> createUsersForSearchResponse(List<UserEntity> userEntities) {
	        return userEntities.stream()
	                .map(ue -> new User(ue.getUsername(), ue.getFirstName(), ue.getLastName(),ue.getDateOfBirth(),ue.getGender(),ue.getPhoneNumber()
	                		,ue.getEmailId())).collect(Collectors.toList());
	    }
	 private User createUserFromEntity(UserEntity ue) {
	        return new User(ue.getUserId(), ue.getUsername(), ue.getFirstName(), ue.getLastName(),
	                ue.getDateOfBirth(), ue.getGender(), ue.getPhoneNumber(), ue.getEmailId(), Role.valueOf(ue.getRole()));
	    }

	public User getUserByUsername(String username) throws ResourceNotFoundException {
		UserEntity userEntity=userRepository.findByUsername(username);
		if(userEntity!=null) {
			return createUserFromEntityForLogin(userEntity);
		}else
		{
			throw new ResourceNotFoundException(null, "User not found for the Username "+username);	
		}		
	}
	 private User createUserFromEntityForLogin(UserEntity ue) {
	        return new User(ue.getUserId(), ue.getUsername(), ue.getPassword(),ue.getFirstName(), ue.getLastName(),
	                ue.getDateOfBirth(), ue.getGender(), ue.getPhoneNumber(), ue.getEmailId(), Role.valueOf(ue.getRole()));
	    }
}
