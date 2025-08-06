package io.naryo.infrastructure.event.mongo.block.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LatestBlockDocumentRepository
        extends MongoRepository<LatestBlockDocument, String> {}
