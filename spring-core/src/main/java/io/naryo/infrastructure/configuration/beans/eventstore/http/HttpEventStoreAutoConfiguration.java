package io.naryo.infrastructure.configuration.beans.eventstore.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.infrastructure.event.http.store.block.BlockHttpBlockEventStore;
import io.naryo.infrastructure.event.http.store.block.ContractEventHttpBlockEventStore;
import io.naryo.infrastructure.event.http.store.block.TransactionHttpBlockEventStore;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public final class HttpEventStoreAutoConfiguration {

    BlockHttpBlockEventStore blockHttpBlockEventStore(
            OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new BlockHttpBlockEventStore(httpClient, objectMapper);
    }

    TransactionHttpBlockEventStore transactionHttpBlockEventStore(
            OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new TransactionHttpBlockEventStore(httpClient, objectMapper);
    }

    ContractEventHttpBlockEventStore contractEventHttpBlockEventStore(
            OkHttpClient httpClient, ObjectMapper objectMapper) {
        return new ContractEventHttpBlockEventStore(httpClient, objectMapper);
    }
}
