package io.naryo.infrastructure.store.event.persistence.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.naryo.infrastructure.store.event.persistence.entity.block.BlockEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockEventEntityRepository extends JpaRepository<BlockEventEntity, UUID> {

    Optional<BlockEventEntity> findFirstByOrderByNumberDesc();

    Optional<BlockEventEntity> findByNumber(BigInteger number);

    List<BlockEventEntity> findAllByNumberIn(List<BigInteger> numbers);
}
