package io.naryo.infrastructure.configuration.beans;

import io.naryo.infrastructure.event.mongo.block.BlockMongoBlockEventStore;
import io.naryo.infrastructure.event.mongo.block.ContractEventMongoBlockEventStore;
import io.naryo.infrastructure.event.mongo.block.TransactionMongoBlockEventStore;
import io.naryo.infrastructure.event.mongo.block.model.LatestBlockDocumentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoEventStoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(BlockMongoBlockEventStore.class)
    BlockMongoBlockEventStore blockMongoBlockEventStore(MongoTemplate mongoTemplate, LatestBlockDocumentRepository repository) {
        return new BlockMongoBlockEventStore(mongoTemplate, repository);
    }

    @Bean
    @ConditionalOnMissingBean(TransactionMongoBlockEventStore.class)
    TransactionMongoBlockEventStore transactionMongoBlockEventStore(MongoTemplate mongoTemplate) {
        return new TransactionMongoBlockEventStore(mongoTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(ContractEventMongoBlockEventStore.class)
    ContractEventMongoBlockEventStore contractEventMongoBlockEventStore(MongoTemplate mongoTemplate) {
        return new ContractEventMongoBlockEventStore(mongoTemplate);
    }
}
