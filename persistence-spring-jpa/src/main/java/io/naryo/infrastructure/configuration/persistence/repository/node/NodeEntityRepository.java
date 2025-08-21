package io.naryo.infrastructure.configuration.persistence.repository.node;

import java.util.UUID;

import io.naryo.infrastructure.configuration.persistence.entity.node.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NodeEntityRepository extends JpaRepository<NodeEntity, UUID> {}
