package io.naryo.infrastructure.event.http.store.block;

import java.math.BigInteger;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.event.store.block.BlockEventStore;
import io.naryo.domain.configuration.eventstore.block.EventStoreTarget;
import io.naryo.domain.configuration.eventstore.block.TargetType;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.infrastructure.event.http.configuration.HttpBlockEventStoreConfiguration;
import io.naryo.infrastructure.event.http.store.HttpBlockEventStore;
import io.naryo.infrastructure.event.http.store.block.model.LatestBlockResponseModel;
import okhttp3.OkHttpClient;

public final class BlockHttpBlockEventStore extends HttpBlockEventStore<BlockEvent>
        implements BlockEventStore<HttpBlockEventStoreConfiguration> {

    public BlockHttpBlockEventStore(OkHttpClient httpClient, ObjectMapper objectMapper) {
        super(httpClient, objectMapper);
    }

    @Override
    public BigInteger getLastBlockEvent(HttpBlockEventStoreConfiguration configuration) {
        Optional<EventStoreTarget> target = findTarget(TargetType.BLOCK, configuration);
        if (target.isEmpty()) {
            return BigInteger.valueOf(-1);
        }
        return get(
                        makeUrl(target.get(), configuration),
                        configuration.getEndpoint().getHeaders(),
                        LatestBlockResponseModel.class)
                .blockNumber();
    }

    @Override
    public boolean supports(Event event) {
        return event instanceof BlockEvent;
    }
}
