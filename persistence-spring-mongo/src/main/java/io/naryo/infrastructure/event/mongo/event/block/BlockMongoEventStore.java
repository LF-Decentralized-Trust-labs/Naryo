package io.naryo.infrastructure.event.mongo.event.block;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.store.event.block.BlockEventStore;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.configuration.eventstore.active.block.MongoStoreConfiguration;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.infrastructure.event.mongo.event.MongoEventStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

@Slf4j
public final class BlockMongoEventStore extends MongoEventStore<BigInteger, BlockEvent>
        implements BlockEventStore<MongoStoreConfiguration> {

    public BlockMongoEventStore(MongoTemplate mongoTemplate) {
        super(BlockEvent.class, mongoTemplate);
    }

    @Override
    protected String getKeyFieldName() {
        return "number";
    }

    @Override
    public Optional<BigInteger> getLatest(MongoStoreConfiguration configuration) {
        try {
            Query query = new Query();
            query.with(Sort.by(Sort.Direction.DESC, getKeyFieldName()));
            query.limit(1);

            BlockEvent latestBlock =
                    mongoTemplate.findOne(query, clazz, getDestination(configuration).value());

            return Optional.ofNullable(latestBlock)
                    .map(BlockEvent::getNumber)
                    .map(NonNegativeBlockNumber::value);
        } catch (Exception e) {
            log.error("Error while fetching latest block event from MongoDB event store", e);
            return Optional.empty();
        }
    }
}
