package io.naryo.infrastructure.event.mongo;

import io.naryo.application.event.store.EventStore;
import io.naryo.domain.configuration.eventstore.active.block.MongoBlockEventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.active.block.TargetType;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.infrastructure.event.mongo.model.BlockEventDocument;
import io.naryo.infrastructure.event.mongo.model.ContractEventDocument;
import io.naryo.infrastructure.event.mongo.model.TransactionEventDocument;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class MongoBlockEventStore<E extends Event>
        implements EventStore<E, MongoBlockEventStoreConfiguration> {

    protected final MongoTemplate mongoTemplate;

    protected MongoBlockEventStore(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(E event, MongoBlockEventStoreConfiguration configuration) {
        TargetType targetType = map(event.getEventType());
        findTarget(targetType, configuration)
                .ifPresent(target -> {
                    switch (event) {
                        case BlockEvent blockEvent ->
                                mongoTemplate.save(BlockEventDocument.from(blockEvent), target.destination().value());
                        case ContractEvent contractEvent ->
                                mongoTemplate.save(ContractEventDocument.from(contractEvent), target.destination().value());
                        case TransactionEvent transactionEvent ->
                                mongoTemplate.save(TransactionEventDocument.from(transactionEvent), target.destination().value());
                        default ->
                                throw new IllegalArgumentException("Unsupported event type: " + event.getEventType());
                    }

                });
    }
}
