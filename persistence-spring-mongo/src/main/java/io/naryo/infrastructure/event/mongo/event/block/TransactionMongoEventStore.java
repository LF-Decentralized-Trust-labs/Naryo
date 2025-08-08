package io.naryo.infrastructure.event.mongo.event.block;


import io.naryo.application.store.event.block.TransactionEventStore;
import io.naryo.domain.configuration.eventstore.active.block.MongoStoreConfiguration;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.infrastructure.event.mongo.event.MongoEventStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

@Slf4j
public final class TransactionMongoEventStore extends MongoEventStore<String, TransactionEvent>
        implements TransactionEventStore<MongoStoreConfiguration> {

    public TransactionMongoEventStore(MongoTemplate mongoTemplate) {
        super(TransactionEvent.class, mongoTemplate);
    }

    @Override
    protected String getKeyFieldName() {
        return "hash";
    }

    @Override
    public Optional<String> getLatest(MongoStoreConfiguration configuration) {
        try {
            Query query = new Query();
            query.with(Sort.by(Sort.Direction.DESC, getKeyFieldName()));
            query.limit(1);

            TransactionEvent latest = mongoTemplate.findOne(
                query,
                clazz,
                getDestination(configuration).value());

            return Optional.ofNullable(latest).map(TransactionEvent::getHash);
        } catch (Exception e) {
            log.error("Error while fetching latest transaction event from MongoDB event store", e);
            return Optional.empty();
        }
    }
}
