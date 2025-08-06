package io.naryo.infrastructure.configuration.persistence.document.event.store;

import io.naryo.domain.configuration.store.StoreState;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("inactive_event_store")
public final class InactiveEventStoreConfigurationPropertiesDocument
        extends EventStoreConfigurationPropertiesDocument {

    public InactiveEventStoreConfigurationPropertiesDocument(String nodeId) {
        super(nodeId, StoreState.INACTIVE);
    }
}
