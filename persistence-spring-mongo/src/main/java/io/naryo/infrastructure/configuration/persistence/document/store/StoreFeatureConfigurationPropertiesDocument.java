package io.naryo.infrastructure.configuration.persistence.document.store;

import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import lombok.Getter;

@Getter
public abstract class StoreFeatureConfigurationPropertiesDocument
        implements StoreFeatureConfigurationDescriptor {}
