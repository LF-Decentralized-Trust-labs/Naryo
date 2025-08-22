package io.naryo.infrastructure.event.http.event.block;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.store.event.block.TransactionEventStore;
import io.naryo.domain.configuration.store.active.http.HttpStoreConfiguration;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.infrastructure.event.http.event.HttpEventStore;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
public final class TransactionHttpEventStore extends HttpEventStore<String, TransactionEvent>
        implements TransactionEventStore<HttpStoreConfiguration> {

    public TransactionHttpEventStore(OkHttpClient httpClient, ObjectMapper objectMapper) {
        super(TransactionEvent.class, httpClient, objectMapper);
    }
}
