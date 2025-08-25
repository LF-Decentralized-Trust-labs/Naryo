package io.naryo.infrastructure.event.mongo;

import java.util.List;
import java.util.Optional;

import io.naryo.application.store.Store;
import io.naryo.domain.common.Destination;
import io.naryo.domain.configuration.eventstore.active.block.MongoStoreConfiguration;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.infrastructure.event.mongo.event.block.model.BlockEventDocument;
import io.naryo.infrastructure.event.mongo.event.block.model.ContractEventDocument;
import io.naryo.infrastructure.event.mongo.event.block.model.TransactionEventDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Slf4j
public abstract class MongoStore<K, D> implements Store<MongoStoreConfiguration, K, D> {

    protected final Class<D> clazz;
    protected final MongoTemplate mongoTemplate;

    protected MongoStore(Class<D> clazz, MongoTemplate mongoTemplate) {
        this.clazz = clazz;
        this.mongoTemplate = mongoTemplate;
    }

    protected abstract Destination getDestination(MongoStoreConfiguration configuration);

    protected abstract String getKeyFieldName();

    @Override
    public void save(MongoStoreConfiguration configuration, K key, D data) {
        if (this.clazz.isAssignableFrom(BlockEvent.class)) {
            mongoTemplate.save(
                    BlockEventDocument.from((BlockEvent) data),
                    getDestination(configuration).value());
        } else if (this.clazz.isAssignableFrom(ContractEvent.class)) {
            mongoTemplate.save(
                    ContractEventDocument.from((ContractEvent) data),
                    getDestination(configuration).value());
        } else if (this.clazz.isAssignableFrom(TransactionEvent.class)) {
            mongoTemplate.save(
                    TransactionEventDocument.from((TransactionEvent) data),
                    getDestination(configuration).value());
        } else {
            throw new IllegalArgumentException("Unsupported type: " + this.clazz);
        }
    }

    @Override
    public Optional<D> get(MongoStoreConfiguration configuration, K key) {
        try {
            return Optional.ofNullable(
                    mongoTemplate.findOne(
                            Query.query(Criteria.where(getKeyFieldName()).is(key)),
                            clazz,
                            getDestination(configuration).value()));
        } catch (Exception e) {
            log.error("Error while processing getting from mongoDB", e);
            return Optional.empty();
        }
    }

    @Override
    public List<D> get(MongoStoreConfiguration configuration, List<K> keys) {
        try {
            if (keys == null || keys.isEmpty()) {
                log.warn("Keys list is null or empty. Returning an empty result.");
                return List.of();
            }

            return mongoTemplate.find(
                    Query.query(Criteria.where(getKeyFieldName()).in(keys)),
                    clazz,
                    getDestination(configuration).value());
        } catch (Exception e) {
            log.error("Error while processing getting from mongoDB", e);
            return List.of();
        }
    }

    @Override
    public boolean supports(StoreType type, Class<?> clazz) {
        return type.getName().equalsIgnoreCase("mongo") && clazz.isAssignableFrom(this.clazz);
    }
}
