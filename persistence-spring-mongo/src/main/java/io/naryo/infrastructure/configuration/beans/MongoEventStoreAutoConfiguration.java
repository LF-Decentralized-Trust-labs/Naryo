package io.naryo.infrastructure.configuration.beans;

import io.naryo.infrastructure.event.mongo.event.block.BlockMongoEventStore;
import io.naryo.infrastructure.event.mongo.event.block.ContractEventMongoEventStore;
import io.naryo.infrastructure.event.mongo.event.block.TransactionMongoEventStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoEventStoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(BlockMongoEventStore.class)
    BlockMongoEventStore blockMongoBlockEventStore(MongoTemplate mongoTemplate) {
        return new BlockMongoEventStore(mongoTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(TransactionMongoEventStore.class)
    TransactionMongoEventStore transactionMongoBlockEventStore(MongoTemplate mongoTemplate) {
        return new TransactionMongoEventStore(mongoTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(ContractEventMongoEventStore.class)
    ContractEventMongoEventStore contractEventMongoBlockEventStore(MongoTemplate mongoTemplate) {
        return new ContractEventMongoEventStore(mongoTemplate);
    }
}
