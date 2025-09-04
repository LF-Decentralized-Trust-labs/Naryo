package io.naryo.infrastructure.configuration.persistence.document.store;

import io.naryo.application.configuration.source.model.store.InactiveStoreConfigurationDescriptor;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("inactive_event_store")
public final class InactiveStoreConfigurationPropertiesDocument
        extends StoreConfigurationPropertiesDocument
        implements InactiveStoreConfigurationDescriptor {

    public InactiveStoreConfigurationPropertiesDocument(String nodeId) {
        super(nodeId);
    }
}
