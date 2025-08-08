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
import io.naryo.infrastructure.event.mongo.model.BlockEventDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

@Slf4j
public final class BlockMongoBlockEventStore extends MongoBlockEventStore<BlockEvent>
        implements BlockEventStore<MongoBlockEventStoreConfiguration> {

    public BlockMongoBlockEventStore(
            MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    @Override
    public Optional<BigInteger> getLatestBlock(MongoBlockEventStoreConfiguration configuration) {
        Optional<EventStoreTarget> target = findTarget(TargetType.BLOCK, configuration);
        Optional<BigInteger> defaultValue = Optional.of(BigInteger.valueOf(-1));
        try {
            BigInteger latestBlock =  target.map(
                    t -> {
                        Query query = new Query();
                        query.with(Sort.by(Sort.Direction.DESC, "number"));
                        query.limit(1);

                        BlockEventDocument lastBlock = this.mongoTemplate.findOne(query, BlockEventDocument.class, t.destination().value());

                        return Optional.ofNullable(lastBlock)
                                .map(BlockEventDocument::getNumber)
                                .orElse(defaultValue.get());
                    }
            ).orElse(defaultValue.get());
            return Optional.of(latestBlock);
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
