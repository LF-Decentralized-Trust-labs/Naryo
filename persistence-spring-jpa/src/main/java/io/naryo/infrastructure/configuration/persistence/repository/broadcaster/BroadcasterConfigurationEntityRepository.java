package io.naryo.infrastructure.configuration.persistence.repository.broadcaster;

import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration.BroadcasterConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BroadcasterConfigurationEntityRepository
        extends JpaRepository<BroadcasterConfigurationEntity, String> {}
