package io.naryo.infrastructure.configuration.persistence.document.store.event;

import io.naryo.application.configuration.source.model.store.event.EventStoreConfigurationDescriptor;
import io.naryo.infrastructure.configuration.persistence.document.store.StoreFeatureConfigurationPropertiesDocument;
import lombok.Getter;

@Getter
public abstract class EventStoreConfigurationPropertiesDocument
        extends StoreFeatureConfigurationPropertiesDocument
        implements EventStoreConfigurationDescriptor {}
