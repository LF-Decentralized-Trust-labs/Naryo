package io.naryo.infrastructure.event.mongo.event.block;

import io.naryo.application.store.event.block.ContractEventEventStore;
import io.naryo.domain.configuration.eventstore.active.block.MongoStoreConfiguration;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.infrastructure.event.mongo.event.MongoEventStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
public final class ContractEventMongoEventStore extends MongoEventStore<String, ContractEvent>
        implements ContractEventEventStore<MongoStoreConfiguration> {

    public ContractEventMongoEventStore(MongoTemplate mongoTemplate) {
        super(ContractEvent.class, mongoTemplate);
    }

    @Override
    protected String getKeyFieldName() {
        return "transactionHash:logIndex";
    }
}
