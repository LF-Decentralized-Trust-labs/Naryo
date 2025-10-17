package io.naryo.api.storeconfiguration.common.request;

import io.naryo.application.configuration.source.model.store.event.EventStoreConfigurationDescriptor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public abstract sealed class EventStoreFeatureConfigurationRequest
        extends StoreFeatureConfigurationRequest implements EventStoreConfigurationDescriptor
        permits BlockEventStoreFeatureConfigurationRequest {}
