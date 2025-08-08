package io.naryo.infrastructure.configuration.persistence.document.store;

import io.naryo.domain.configuration.store.StoreState;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("inactive_event_store")
public final class InactiveStoreConfigurationPropertiesDocument
        extends StoreConfigurationPropertiesDocument {

    public InactiveStoreConfigurationPropertiesDocument(String nodeId) {
        super(nodeId, StoreState.INACTIVE);
    }
}
