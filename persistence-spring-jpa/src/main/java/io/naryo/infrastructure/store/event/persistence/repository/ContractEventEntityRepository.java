package io.naryo.infrastructure.store.event.persistence.repository;

import io.naryo.infrastructure.store.event.persistence.entity.contract.ContractEventEntity;
import io.naryo.infrastructure.store.event.persistence.entity.contract.ContractEventEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractEventEntityRepository
        extends JpaRepository<ContractEventEntity, ContractEventEntityId> {}
