package io.naryo.domain.configuration.eventstore.inactive;

import java.util.UUID;

import io.naryo.domain.configuration.eventstore.EventStoreConfiguration;
import io.naryo.domain.configuration.eventstore.EventStoreState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public final class InactiveEventStoreConfiguration extends EventStoreConfiguration {

    public InactiveEventStoreConfiguration(UUID nodeId) {
        super(nodeId, EventStoreState.INACTIVE);
    }
}
