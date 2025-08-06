package io.naryo.infrastructure.event.mongo.block;

import io.naryo.application.event.store.block.TransactionEventStore;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.active.block.MongoBlockEventStoreConfiguration;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.infrastructure.event.mongo.MongoBlockEventStore;
import org.springframework.data.mongodb.core.MongoTemplate;

public final class TransactionMongoBlockEventStore extends MongoBlockEventStore<TransactionEvent>
        implements TransactionEventStore<MongoBlockEventStoreConfiguration> {

    public TransactionMongoBlockEventStore(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    @Override
    public boolean supports(Event event, EventStoreConfiguration configuration) {
        return event instanceof TransactionEvent
                && configuration instanceof MongoBlockEventStoreConfiguration;
    }
}
