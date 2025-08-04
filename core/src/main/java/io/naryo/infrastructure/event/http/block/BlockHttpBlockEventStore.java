package io.naryo.infrastructure.event.http.block;

import java.math.BigInteger;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.event.store.block.BlockEventStore;
import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.active.block.EventStoreTarget;
import io.naryo.domain.configuration.eventstore.active.block.TargetType;
import io.naryo.domain.configuration.eventstore.active.block.http.HttpBlockEventStoreConfiguration;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.infrastructure.event.http.HttpBlockEventStore;
import io.naryo.infrastructure.event.http.block.model.LatestBlockResponseModel;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
public final class BlockHttpBlockEventStore extends HttpBlockEventStore<BlockEvent>
        implements BlockEventStore<HttpBlockEventStoreConfiguration> {

    public BlockHttpBlockEventStore(OkHttpClient httpClient, ObjectMapper objectMapper) {
        super(httpClient, objectMapper);
    }

    @Override
    public Optional<BigInteger> getLastestBlock(HttpBlockEventStoreConfiguration configuration) {
        Optional<EventStoreTarget> target = findTarget(TargetType.BLOCK, configuration);
        Optional<BigInteger> defaultValue = Optional.of(BigInteger.valueOf(-1));
        if (target.isEmpty()) {
            return defaultValue;
        }
        try {
            return Optional.ofNullable(
                    get(
                                    makeUrl(target.get(), configuration)
                                            .newBuilder()
                                            .addPathSegment("latest")
                                            .build(),
                                    configuration.getEndpoint().getHeaders(),
                                    LatestBlockResponseModel.class)
                            .blockNumber());
        } catch (Exception e) {
            log.error("Error while fetching latest block from HTTP event store", e);
            return defaultValue;
        }
    }

    @Override
    public boolean supports(Event event, EventStoreConfiguration configuration) {
        return event instanceof BlockEvent
                && configuration instanceof HttpBlockEventStoreConfiguration;
    }
}
