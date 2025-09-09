package io.naryo.infrastructure.store.event;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.naryo.application.store.event.block.BlockEventStore;
import io.naryo.domain.configuration.store.active.JpaActiveStoreConfiguration;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.infrastructure.store.event.persistence.entity.block.BlockEventEntity;
import io.naryo.infrastructure.store.event.persistence.repository.BlockEventEntityRepository;
import org.springframework.stereotype.Component;

@Component
public final class BlockJpaEventStore extends JpaEventStore<BigInteger, BlockEvent>
        implements BlockEventStore<JpaActiveStoreConfiguration> {

    private final BlockEventEntityRepository blockEventEntityRepository;

    public BlockJpaEventStore(BlockEventEntityRepository blockEventEntityRepository) {
        this.blockEventEntityRepository = blockEventEntityRepository;
    }

    @Override
    protected Class<?> getTargetDataClass() {
        return BlockEvent.class;
    }

    @Override
    public Optional<BigInteger> getLatest(JpaActiveStoreConfiguration configuration) {
        return blockEventEntityRepository
                .findFirstByOrderByNumberDesc()
                .map(BlockEventEntity::getNumber);
    }

    @Override
    public void save(JpaActiveStoreConfiguration configuration, BigInteger key, BlockEvent data) {
        blockEventEntityRepository.save(BlockEventEntity.fromBlockEvent(data));
    }

    @Override
    public Optional<BlockEvent> get(JpaActiveStoreConfiguration configuration, BigInteger key) {
        return blockEventEntityRepository.findByNumber(key).map(BlockEventEntity::toBlockEvent);
    }

    @Override
    public List<BlockEvent> get(JpaActiveStoreConfiguration configuration, List<BigInteger> keys) {
        return blockEventEntityRepository.findAllByNumberIn(keys).stream()
                .map(BlockEventEntity::toBlockEvent)
                .collect(Collectors.toList());
    }
}
