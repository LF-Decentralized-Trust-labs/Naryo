package io.naryo.api.storeconfiguration.common.request;

import io.naryo.application.configuration.source.model.store.event.EventStoreConfigurationDescriptor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;

@Schema(description = "Base event store feature configuration request")
@EqualsAndHashCode(callSuper = true)
public abstract class EventStoreConfigurationRequest extends StoreFeatureConfigurationRequest
        implements EventStoreConfigurationDescriptor {}
