package io.naryo.infrastructure.store.event.persistence.repository;

import java.util.UUID;

import io.naryo.infrastructure.store.event.persistence.entity.contract.ContractEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractEventEntityRepository extends JpaRepository<ContractEventEntity, UUID> {}
