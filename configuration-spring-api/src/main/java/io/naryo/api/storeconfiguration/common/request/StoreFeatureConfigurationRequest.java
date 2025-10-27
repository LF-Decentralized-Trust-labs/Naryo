package io.naryo.api.storeconfiguration.common.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FilterStoreConfigurationRequest.class, name = "FILTER_SYNC"),
    @JsonSubTypes.Type(value = BlockEventStoreConfigurationRequest.class, name = "EVENT_BLOCK"),
})
@Schema(
        description = "Base class for store feature configuration request",
        discriminatorProperty = "type",
        discriminatorMapping = {
            @DiscriminatorMapping(
                    value = "FILTER_SYNC",
                    schema = FilterStoreConfigurationRequest.class),
            @DiscriminatorMapping(
                    value = "EVENT_BLOCK",
                    schema = BlockEventStoreConfigurationRequest.class)
        })
@EqualsAndHashCode
public abstract class StoreFeatureConfigurationRequest
        implements StoreFeatureConfigurationDescriptor {}
