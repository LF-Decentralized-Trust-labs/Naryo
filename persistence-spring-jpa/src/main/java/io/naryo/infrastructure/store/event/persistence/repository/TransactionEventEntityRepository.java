package io.naryo.infrastructure.store.event.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.naryo.infrastructure.store.event.persistence.entity.transaction.TransactionEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionEventEntityRepository
        extends JpaRepository<TransactionEventEntity, UUID> {

    Optional<TransactionEventEntity> findByHash(String hash);

    List<TransactionEventEntity> findAllByHashIn(List<String> hashes);
}
