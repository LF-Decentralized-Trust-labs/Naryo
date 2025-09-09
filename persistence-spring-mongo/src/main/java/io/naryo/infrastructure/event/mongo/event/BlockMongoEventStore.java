package io.naryo.infrastructure.event.mongo.event;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.naryo.application.store.event.block.BlockEventStore;
import io.naryo.domain.configuration.eventstore.active.block.MongoStoreConfiguration;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.infrastructure.event.mongo.event.persistence.document.BlockEventDocument;
import io.naryo.infrastructure.event.mongo.event.persistence.repository.BlockEventDocumentRepository;
import org.springframework.stereotype.Component;

@Component
public final class BlockMongoEventStore extends MongoEventStore<BigInteger, BlockEvent>
        implements BlockEventStore<MongoStoreConfiguration> {

    private final BlockEventDocumentRepository blockEventDocumentRepository;

    public BlockMongoEventStore(BlockEventDocumentRepository blockEventDocumentRepository) {
        this.blockEventDocumentRepository = blockEventDocumentRepository;
    }

    @Override
    protected Class<?> getTargetDataClass() {
        return BlockEvent.class;
    }

    @Override
    public Optional<BigInteger> getLatest(MongoStoreConfiguration configuration) {
        return blockEventDocumentRepository
                .findFirstByOrderByNumberDesc()
                .map(BlockEventDocument::getNumber)
                .map(BigInteger::new);
    }

    @Override
    public void save(MongoStoreConfiguration configuration, BigInteger key, BlockEvent data) {
        blockEventDocumentRepository.save(BlockEventDocument.fromBlockEvent(data));
    }

    @Override
    public Optional<BlockEvent> get(MongoStoreConfiguration configuration, BigInteger key) {
        return blockEventDocumentRepository.findByNumber(key).map(BlockEventDocument::toBlockEvent);
    }

    @Override
    public List<BlockEvent> get(MongoStoreConfiguration configuration, List<BigInteger> keys) {
        return blockEventDocumentRepository.findAllByNumberIn(keys).stream()
                .map(BlockEventDocument::toBlockEvent)
                .collect(Collectors.toList());
    }
}
