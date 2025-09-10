package io.naryo.infrastructure.configuration.persistence.repository.event.store;

import java.util.UUID;

import io.naryo.infrastructure.configuration.persistence.entity.store.StoreConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventStoreEntityRepository extends JpaRepository<StoreConfigurationEntity, UUID> {}
