package io.naryo.infrastructure.configuration.persistence.repository.http;

import java.util.UUID;

import io.naryo.infrastructure.configuration.persistence.entity.http.HttpClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HttpClientEntityRepository extends JpaRepository<HttpClientEntity, UUID> {}
