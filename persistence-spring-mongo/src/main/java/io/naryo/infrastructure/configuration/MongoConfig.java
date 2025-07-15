package io.naryo.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@Configuration
@EnableMongoRepositories(basePackages = "io.naryo.infrastructure.configuration")
@EntityScan(basePackages = "io.naryo.infrastructure.configuration.persistence.document")
public class MongoConfig {

}

