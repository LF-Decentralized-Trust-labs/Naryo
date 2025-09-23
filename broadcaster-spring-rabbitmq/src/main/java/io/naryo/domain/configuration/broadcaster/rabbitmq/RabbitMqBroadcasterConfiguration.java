package io.naryo.domain.configuration.broadcaster.rabbitmq;

import java.util.Objects;
import java.util.UUID;

import io.naryo.domain.configuration.broadcaster.BroadcasterCache;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import static io.naryo.domain.RabbitMqConstants.RABBITMQ_TYPE;

@Getter
@SuperBuilder(toBuilder = true)
public final class RabbitMqBroadcasterConfiguration extends BroadcasterConfiguration {

    private final Exchange exchange;
    private final RoutingKey routingKey;

    public RabbitMqBroadcasterConfiguration(
            UUID id, BroadcasterCache cache, Exchange exchange, RoutingKey routingKey) {
        super(id, () -> RABBITMQ_TYPE, cache);
        Objects.requireNonNull(exchange, "Exchange must not be null");
        Objects.requireNonNull(routingKey, "RoutingKey must not be null");
        this.exchange = exchange;
        this.routingKey = routingKey;
    }
}
