package io.naryo.infrastructure.configuration.persistence.repository.store;

import java.util.UUID;

import io.naryo.infrastructure.configuration.persistence.entity.store.StoreConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreConfigurationEntityRepository
        extends JpaRepository<StoreConfigurationEntity, UUID> {}
