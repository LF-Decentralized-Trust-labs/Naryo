package io.naryo.infrastructure.configuration.persistence.repository.broadcaster;

import io.naryo.infrastructure.configuration.persistence.entity.broadcaster.target.BroadcasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BroadcasterEntityRepository extends JpaRepository<BroadcasterEntity, String> {}
