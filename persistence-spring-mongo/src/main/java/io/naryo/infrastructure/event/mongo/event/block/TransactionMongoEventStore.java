package io.naryo.infrastructure.event.mongo.event.block;

import io.naryo.application.store.event.block.TransactionEventStore;
import io.naryo.domain.configuration.eventstore.active.block.MongoStoreConfiguration;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.infrastructure.event.mongo.event.MongoEventStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;

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
}
