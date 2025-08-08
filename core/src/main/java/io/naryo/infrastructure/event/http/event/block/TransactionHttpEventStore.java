package io.naryo.infrastructure.event.http.event.block;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.store.event.block.TransactionEventStore;
import io.naryo.domain.configuration.store.active.http.HttpStoreConfiguration;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.infrastructure.event.http.event.HttpEventStore;
import io.naryo.infrastructure.event.http.event.block.model.LatestTransactionResponseModel;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
public final class TransactionHttpEventStore extends HttpEventStore<String, TransactionEvent>
        implements TransactionEventStore<HttpStoreConfiguration> {

    public TransactionHttpEventStore(OkHttpClient httpClient, ObjectMapper objectMapper) {
        super(TransactionEvent.class, httpClient, objectMapper);
    }

    @Override
    public Optional<String> getLatest(HttpStoreConfiguration configuration) {
        try {
            return Optional.of(
                    get(
                                    makeUrl(getDestination(configuration), configuration)
                                            .newBuilder()
                                            .addPathSegment("latest")
                                            .build(),
                                    configuration.getEndpoint().getHeaders(),
                                    LatestTransactionResponseModel.class)
                            .transactionHash());
        } catch (JsonProcessingException e) {
            log.error("Error while processing JSON response", e);
            return Optional.empty();
        }
    }
}
