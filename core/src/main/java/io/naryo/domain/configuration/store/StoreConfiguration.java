package io.naryo.domain.configuration.store;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public abstract class StoreConfiguration {

    private final UUID nodeId;
    private final StoreState state;

    protected StoreConfiguration(UUID nodeId, StoreState state) {
        this.nodeId = nodeId;
        this.state = state;
    }
}
