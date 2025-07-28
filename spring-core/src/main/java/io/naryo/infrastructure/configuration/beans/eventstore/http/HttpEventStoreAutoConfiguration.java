package io.naryo.infrastructure.configuration.beans.eventstore.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.infrastructure.event.http.block.BlockHttpBlockEventStore;
import io.naryo.infrastructure.event.http.block.ContractEventHttpBlockEventStore;
import io.naryo.infrastructure.event.http.block.TransactionHttpBlockEventStore;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpEventStoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(BlockHttpBlockEventStore.class)
    BlockHttpBlockEventStore blockHttpBlockEventStore(
            OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new BlockHttpBlockEventStore(httpClient, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(TransactionHttpBlockEventStore.class)
    TransactionHttpBlockEventStore transactionHttpBlockEventStore(
            OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new TransactionHttpBlockEventStore(httpClient, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(ContractEventHttpBlockEventStore.class)
    ContractEventHttpBlockEventStore contractEventHttpBlockEventStore(
            OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new ContractEventHttpBlockEventStore(httpClient, objectMapper);
    }
}
