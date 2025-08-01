package io.naryo.infrastructure.configuration.persistence.repository.node;

import io.naryo.infrastructure.configuration.persistence.entity.node.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NodeEntityRepository extends JpaRepository<NodeEntity, UUID> {
}
