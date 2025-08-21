package io.naryo.infrastructure.configuration.persistence.repository.broadcaster;

import java.util.UUID;

import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.BroadcasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BroadcasterEntityRepository extends JpaRepository<BroadcasterEntity, UUID> {}
