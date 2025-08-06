package io.naryo.infrastructure.event.mongo.block;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.event.store.block.BlockEventStore;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.active.block.EventStoreTarget;
import io.naryo.domain.configuration.eventstore.active.block.MongoBlockEventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.active.block.TargetType;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.infrastructure.event.mongo.MongoBlockEventStore;
import io.naryo.infrastructure.event.mongo.block.model.LatestBlockDocument;
import io.naryo.infrastructure.event.mongo.block.model.LatestBlockDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
public final class BlockMongoBlockEventStore extends MongoBlockEventStore<BlockEvent>
        implements BlockEventStore<MongoBlockEventStoreConfiguration> {

    private final LatestBlockDocumentRepository latestBlockDocumentRepository;

    public BlockMongoBlockEventStore(
            MongoTemplate mongoTemplate,
            LatestBlockDocumentRepository latestBlockDocumentRepository) {
        super(mongoTemplate);
        this.latestBlockDocumentRepository = latestBlockDocumentRepository;
    }

    @Override
    public Optional<BigInteger> getLatestBlock(MongoBlockEventStoreConfiguration configuration) {
        Optional<EventStoreTarget> target = findTarget(TargetType.BLOCK, configuration);
        Optional<BigInteger> defaultValue = Optional.of(BigInteger.valueOf(-1));
        if (target.isEmpty()) {
            return defaultValue;
        }
        try {
            Optional<LatestBlockDocument> latestBlockDocument =
                    this.latestBlockDocumentRepository.findById(
                            configuration.getNodeId().toString());
            return latestBlockDocument.map(LatestBlockDocument::getBlockNumber).or(Optional::empty);
        } catch (Exception e) {
            log.error("Error while fetching latest block from MongoDB event store", e);
            return defaultValue;
        }
    }

    @Override
    public boolean supports(Event event, EventStoreConfiguration configuration) {
        return event instanceof BlockEvent
                && configuration instanceof MongoBlockEventStoreConfiguration;
    }
}
