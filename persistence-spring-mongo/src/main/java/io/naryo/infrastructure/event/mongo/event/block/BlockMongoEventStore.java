package io.naryo.infrastructure.event.mongo.event.block;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.store.event.block.BlockEventStore;
import io.naryo.domain.configuration.eventstore.active.block.MongoStoreConfiguration;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.infrastructure.event.mongo.event.MongoEventStore;
import io.naryo.infrastructure.event.mongo.event.block.model.BlockEventDocument;
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

            BlockEventDocument latestBlock =
                    mongoTemplate.findOne(
                            query, BlockEventDocument.class, getDestination(configuration).value());

            return Optional.ofNullable(latestBlock)
                    .map(BlockEventDocument::getNumber)
                    .map(BigInteger::new);
        } catch (Exception e) {
            log.error("Error while fetching latest block event from MongoDB event store", e);
            return Optional.empty();
        }
    }
}
