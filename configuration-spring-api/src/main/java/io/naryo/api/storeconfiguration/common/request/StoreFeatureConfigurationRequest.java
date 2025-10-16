package io.naryo.api.storeconfiguration.common.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import lombok.EqualsAndHashCode;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FilterStoreFeatureConfigurationRequest.class, name = "FILTER_SYNC"),
    @JsonSubTypes.Type(
            value = BlockEventStoreFeatureConfigurationRequest.class,
            name = "EVENT_BLOCK"),
})
@EqualsAndHashCode
public abstract sealed class StoreFeatureConfigurationRequest
        implements StoreFeatureConfigurationDescriptor
        permits FilterStoreFeatureConfigurationRequest, EventStoreFeatureConfigurationRequest {}
