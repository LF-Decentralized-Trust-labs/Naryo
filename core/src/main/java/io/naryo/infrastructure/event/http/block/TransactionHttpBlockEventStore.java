package io.naryo.infrastructure.event.http.block;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.event.store.block.TransactionEventStore;
import io.naryo.domain.configuration.eventstore.block.server.http.HttpBlockEventStoreConfiguration;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.infrastructure.event.http.HttpBlockEventStore;
import okhttp3.OkHttpClient;

public final class TransactionHttpBlockEventStore extends HttpBlockEventStore<TransactionEvent>
        implements TransactionEventStore<HttpBlockEventStoreConfiguration> {

    public TransactionHttpBlockEventStore(OkHttpClient httpClient, ObjectMapper objectMapper) {
        super(httpClient, objectMapper);
    }

    @Override
    public boolean supports(Event event) {
        return event instanceof TransactionEvent;
    }
}
