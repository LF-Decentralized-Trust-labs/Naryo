package io.naryo.infrastructure.store.event.persistence.repository;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

import io.naryo.infrastructure.store.event.persistence.entity.contract.ContractEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractEventEntityRepository extends JpaRepository<ContractEventEntity, UUID> {

    Optional<ContractEventEntity> findByBlockHashAndLogIndex(String blockHash, BigInteger logIndex);
}
