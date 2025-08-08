package io.naryo.domain.configuration.store.inactive;

import java.util.UUID;

import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public final class InactiveStoreConfiguration extends StoreConfiguration {

    public InactiveStoreConfiguration(UUID nodeId) {
        super(nodeId, StoreState.INACTIVE);
    }
}
