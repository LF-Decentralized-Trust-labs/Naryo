package io.naryo.infrastructure.configuration.persistence.repository.filter;

import java.util.UUID;

import io.naryo.infrastructure.configuration.persistence.entity.filter.FilterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepository extends JpaRepository<FilterEntity, UUID> {}
