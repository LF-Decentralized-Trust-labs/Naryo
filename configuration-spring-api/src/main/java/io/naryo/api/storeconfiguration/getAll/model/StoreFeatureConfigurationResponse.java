package io.naryo.api.storeconfiguration.getAll.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.api.storeconfiguration.common.request.BlockEventStoreConfigurationRequest;
import io.naryo.api.storeconfiguration.common.request.FilterStoreConfigurationRequest;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.event.block.BlockEventStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.filter.FilterStoreConfiguration;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FilterStoreConfigurationResponse.class, name = "FILTER_SYNC"),
    @JsonSubTypes.Type(
        value = BlockEventStoreConfigurationResponse.class,
        name = "EVENT_BLOCK"),
})
@Schema(
    description = "Base class for store feature configuration",
    discriminatorProperty = "type",
    discriminatorMapping = {
        @DiscriminatorMapping(
            value = "FILTER_SYNC",
            schema = FilterStoreConfigurationResponse.class),
        @DiscriminatorMapping(
            value = "EVENT_BLOCK",
            schema = BlockEventStoreConfigurationResponse.class)
    })
@Getter
public abstract class StoreFeatureConfigurationResponse {

    public static StoreFeatureConfigurationResponse fromDomain(
            StoreFeatureConfiguration storeFeatureConfiguration) {
        return switch (storeFeatureConfiguration) {
            case BlockEventStoreConfiguration blockEventStoreConfiguration ->
                    BlockEventStoreConfigurationResponse.fromDomain(blockEventStoreConfiguration);
            case FilterStoreConfiguration filterStoreConfiguration ->
                    FilterStoreConfigurationResponse.fromDomain(filterStoreConfiguration);
            default ->
                    throw new IllegalStateException(
                            "Unexpected value: " + storeFeatureConfiguration);
        };
    }
}
