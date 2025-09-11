package io.naryo.infrastructure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "io.naryo.infrastructure")
@EntityScan(basePackages = "io.naryo.infrastructure")
public class MongoPersistenceConfig {}
