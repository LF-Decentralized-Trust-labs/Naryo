package io.naryo.infrastructure.event.mongo.block;

import io.naryo.application.event.store.block.ContractEventEventStore;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.active.block.MongoBlockEventStoreConfiguration;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.infrastructure.event.mongo.MongoBlockEventStore;
import org.springframework.data.mongodb.core.MongoTemplate;

public final class ContractEventMongoBlockEventStore extends MongoBlockEventStore<ContractEvent>
        implements ContractEventEventStore<MongoBlockEventStoreConfiguration> {

    public ContractEventMongoBlockEventStore(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    @Override
    public boolean supports(Event event, EventStoreConfiguration configuration) {
        return event instanceof ContractEvent
                && configuration instanceof MongoBlockEventStoreConfiguration;
    }
}
