package org.blub.repository;

import org.blub.domain.DDocument;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the DDocument entity.
 */
public interface DDocumentRepository extends MongoRepository<DDocument,String> {

}
