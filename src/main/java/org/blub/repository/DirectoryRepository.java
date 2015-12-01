package org.blub.repository;

import org.blub.domain.Directory;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Directory entity.
 */
public interface DirectoryRepository extends MongoRepository<Directory,String> {

}
