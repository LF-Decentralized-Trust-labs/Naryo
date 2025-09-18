package io.naryo.infrastructure.configuration.persistence.repository.http;

import io.naryo.infrastructure.configuration.persistence.document.http.HttpClientDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HttpClientDocumentRepository extends MongoRepository<HttpClientDocument, String> {}
