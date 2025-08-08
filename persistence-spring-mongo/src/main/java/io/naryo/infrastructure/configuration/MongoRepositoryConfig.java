package io.naryo.infrastructure.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "io.naryo.infrastructure.configuration.persistence.repository")
@EntityScan(basePackages = {
    "io.naryo.infrastructure.event.mongo.event.block.model",
    "io.naryo.infrastructure.configuration.persistence.document"
})
public class MongoRepositoryConfig {}
