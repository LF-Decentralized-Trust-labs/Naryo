package io.naryo.infrastructure.broadcaster;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataBroadcasterRepository extends MongoRepository<BroadcasterDocument, String> {
}
