package io.naryo.infrastructure.event.http.event.block;

import java.math.BigInteger;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.store.event.block.BlockEventStore;
import io.naryo.domain.configuration.store.active.http.HttpStoreConfiguration;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.infrastructure.event.http.event.HttpEventStore;
import io.naryo.infrastructure.event.http.event.block.model.LatestBlockResponseModel;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
public final class BlockHttpEventStore extends HttpEventStore<BigInteger, BlockEvent>
        implements BlockEventStore<HttpStoreConfiguration> {

    public BlockHttpEventStore(OkHttpClient httpClient, ObjectMapper objectMapper) {
        super(BlockEvent.class, httpClient, objectMapper);
    }

    @Override
    public Optional<BigInteger> getLatest(HttpStoreConfiguration configuration) {
        try {
            return Optional.of(
                    get(
                                    makeUrl(getDestination(configuration), configuration)
                                            .newBuilder()
                                            .addPathSegment("latest")
                                            .build(),
                                    configuration.getEndpoint().getHeaders(),
                                    LatestBlockResponseModel.class)
                            .blockNumber());
        } catch (JsonProcessingException e) {
            log.error("Error while processing JSON response", e);
            return Optional.empty();
        }
    }
}
