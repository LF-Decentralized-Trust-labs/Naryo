package io.naryo.infrastructure.event.http.block;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.event.store.block.ContractEventEventStore;
import io.naryo.domain.configuration.eventstore.block.server.http.HttpBlockEventStoreConfiguration;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.infrastructure.event.http.HttpBlockEventStore;
import okhttp3.OkHttpClient;

public final class ContractEventHttpBlockEventStore extends HttpBlockEventStore<ContractEvent>
        implements ContractEventEventStore<HttpBlockEventStoreConfiguration> {

    public ContractEventHttpBlockEventStore(OkHttpClient httpClient, ObjectMapper objectMapper) {
        super(httpClient, objectMapper);
    }

    @Override
    public boolean supports(Event event) {
        return event instanceof ContractEvent;
    }
}
