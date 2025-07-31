package io.naryo.infrastructure.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "io.naryo.infrastructure.configuration.persistence.entity")
@EnableJpaRepositories(
        basePackages = "io.naryo.infrastructure.configuration.persistence.repository")
public class JpaPersistenceConfig {}
