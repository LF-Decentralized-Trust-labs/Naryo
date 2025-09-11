package io.naryo.infrastructure.configuration.beans.store.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.infrastructure.event.http.event.block.BlockHttpEventStore;
import io.naryo.infrastructure.event.http.event.block.ContractEventHttpEventStore;
import io.naryo.infrastructure.event.http.event.block.TransactionHttpEventStore;
import io.naryo.infrastructure.event.http.filter.HttpFilterStore;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpStoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(BlockHttpEventStore.class)
    BlockHttpEventStore blockHttpBlockEventStore(
            OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new BlockHttpEventStore(httpClient, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(TransactionHttpEventStore.class)
    TransactionHttpEventStore transactionHttpBlockEventStore(
            OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new TransactionHttpEventStore(httpClient, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(ContractEventHttpEventStore.class)
    ContractEventHttpEventStore contractEventHttpBlockEventStore(
            OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new ContractEventHttpEventStore(httpClient, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(HttpFilterStore.class)
    HttpFilterStore filterStore(OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new HttpFilterStore(httpClient, objectMapper);
    }
}
