package io.naryo.infrastructure.event.mongo;

import io.naryo.application.event.store.EventStore;
import io.naryo.domain.configuration.eventstore.active.block.MongoBlockEventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.active.block.TargetType;
import io.naryo.domain.event.Event;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class MongoBlockEventStore<E extends Event>
    implements EventStore<E, MongoBlockEventStoreConfiguration> {

    private final MongoTemplate mongoTemplate;

    protected MongoBlockEventStore(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(E event, MongoBlockEventStoreConfiguration configuration) {
        TargetType targetType = map(event.getEventType());
        findTarget(targetType, configuration).ifPresent(
            target -> this.mongoTemplate.save(event, target.destination().value())
        );
    }

}
