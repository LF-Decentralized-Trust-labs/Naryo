package io.naryo.domain.configuration.broadcaster;

import java.util.UUID;

import lombok.Getter;

import static io.naryo.domain.KafkaConstants.KAFKA_TYPE;

@Getter
public final class KafkaBroadcasterConfiguration extends BroadcasterConfiguration {

    public KafkaBroadcasterConfiguration(UUID id, BroadcasterCache cache) {
        super(id, () -> KAFKA_TYPE, cache);
    }
}
