package io.naryo.infrastructure.event.http.event;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.store.event.EventStore;
import io.naryo.domain.common.Destination;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.active.feature.event.block.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.event.block.EventStoreTarget;
import io.naryo.domain.configuration.store.active.feature.event.block.TargetType;
import io.naryo.domain.configuration.store.active.http.HttpStoreConfiguration;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.infrastructure.event.http.HttpStore;
import okhttp3.OkHttpClient;

public abstract class HttpEventStore<K, D extends Event<?>> extends HttpStore<K, D>
        implements EventStore<HttpStoreConfiguration, K, D> {

    public HttpEventStore(Class<D> clazz, OkHttpClient httpClient, ObjectMapper objectMapper) {
        super(clazz, httpClient, objectMapper);
    }

    @Override
    protected Destination getDestination(HttpStoreConfiguration configuration) {
        TargetType targetType = resolveTargetType(clazz);
        return findTarget(targetType, configuration)
                .orElseThrow(
                        () -> new IllegalArgumentException("Target not found for: " + targetType))
                .destination();
    }

    private Optional<EventStoreTarget> findTarget(
            TargetType targetType, HttpStoreConfiguration configuration) {
        BlockEventStoreConfiguration blockConfig = configuration.getFeature(StoreFeatureType.EVENT);
        return blockConfig.getTargets().stream()
                .filter(target -> target.type().equals(targetType))
                .findFirst();
    }

    private TargetType resolveTargetType(Class<? extends Event<?>> eventClass) {
        if (eventClass.isAssignableFrom(BlockEvent.class)) {
            return TargetType.BLOCK;
        } else if (eventClass.isAssignableFrom(TransactionEvent.class)) {
            return TargetType.TRANSACTION;
        } else if (eventClass.isAssignableFrom(ContractEvent.class)) {
            return TargetType.CONTRACT_EVENT;
        } else {
            throw new IllegalArgumentException("Unsupported event class: " + eventClass);
        }
    }
}
