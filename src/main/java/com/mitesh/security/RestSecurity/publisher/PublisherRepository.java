package com.mitesh.security.RestSecurity.publisher;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends CrudRepository<PublisherEntity, Integer>{

	List<PublisherEntity> findByNameContaining(String name);
}
