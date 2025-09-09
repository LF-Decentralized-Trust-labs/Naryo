package io.naryo.infrastructure.store.event.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.naryo.infrastructure.store.event.persistence.entity.contract.ContractEventEntity;
import io.naryo.infrastructure.store.event.persistence.entity.contract.ContractEventEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractEventEntityRepository extends JpaRepository<ContractEventEntity, UUID> {

    Optional<ContractEventEntity> findById(ContractEventEntityId id);

    List<ContractEventEntity> findAllByIdIn(List<ContractEventEntityId> ids);
}
