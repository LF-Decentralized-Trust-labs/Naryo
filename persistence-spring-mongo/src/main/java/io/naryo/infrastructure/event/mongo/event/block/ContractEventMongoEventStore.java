package io.naryo.infrastructure.event.mongo.event.block;

import io.naryo.application.store.event.block.ContractEventEventStore;
import io.naryo.domain.configuration.eventstore.active.block.MongoStoreConfiguration;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.infrastructure.event.mongo.event.MongoEventStore;
import io.naryo.infrastructure.event.mongo.event.block.model.ContractEventDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

@Slf4j
public final class ContractEventMongoEventStore extends MongoEventStore<String, ContractEvent>
    implements ContractEventEventStore<MongoStoreConfiguration> {

    public ContractEventMongoEventStore(MongoTemplate mongoTemplate) {
        super(ContractEvent.class, mongoTemplate);
    }

    @Override
    protected String getKeyFieldName() {
        return "transactionHash";
    }

    @Override
    public Optional<String> getLatest(MongoStoreConfiguration configuration) {
        try {
            Query query = new Query();
            query.with(Sort.by(Sort.Direction.DESC, getKeyFieldName()));
            query.limit(1);

            ContractEventDocument latest = mongoTemplate.findOne(
                query,
                ContractEventDocument.class,
                getDestination(configuration).value());

            return Optional.ofNullable(latest).map(ContractEventDocument::getTransactionHash);
        } catch (Exception e) {
            log.error("Error while fetching latest contract event from MongoDB event store", e);
            return Optional.empty();
        }
    }
}
