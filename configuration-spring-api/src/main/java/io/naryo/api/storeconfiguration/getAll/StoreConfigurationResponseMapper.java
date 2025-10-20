package io.naryo.api.storeconfiguration.getAll;

import java.util.*;
import java.util.stream.Collectors;

import io.naryo.api.storeconfiguration.common.response.ActiveStoreConfigurationResponse;
import io.naryo.api.storeconfiguration.common.response.InactiveStoreConfigurationResponse;
import io.naryo.api.storeconfiguration.common.response.StoreConfigurationResponse;
import io.naryo.api.storeconfiguration.common.response.StoreFeatureConfigurationResponse;
import io.naryo.application.store.configuration.mapper.ActiveStoreConfigurationAdditionalPropertiesMapperRegistry;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.inactive.InactiveStoreConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public final class StoreConfigurationResponseMapper {
    private final ActiveStoreConfigurationAdditionalPropertiesMapperRegistry
            additionalPropertiesMapperRegistry;

    public List<StoreConfigurationResponse> map(
            Collection<StoreConfiguration> storeConfigurations, Map<UUID, String> fingerprints) {
        return storeConfigurations.stream()
                .map(
                        storeConfiguration -> {
                            String currentItemHash =
                                    fingerprints.get(storeConfiguration.getNodeId());
                            return map(storeConfiguration, currentItemHash);
                        })
                .collect(Collectors.toList());
    }

    public StoreConfigurationResponse map(
            StoreConfiguration storeConfiguration, String currentItemHash) {
        return switch (storeConfiguration) {
            case ActiveStoreConfiguration active -> mapActive(active, currentItemHash);
            case InactiveStoreConfiguration inactive ->
                    InactiveStoreConfigurationResponse.map(inactive, currentItemHash);
            default -> throw new IllegalStateException("Unexpected value: " + storeConfiguration);
        };
    }

    private ActiveStoreConfigurationResponse mapActive(
            ActiveStoreConfiguration activeStoreConfiguration, String currentItemHash) {
        Map<StoreFeatureType, StoreFeatureConfigurationResponse> features =
                activeStoreConfiguration.getFeatures().entrySet().stream()
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        entry ->
                                                StoreFeatureConfigurationResponse.map(
                                                        entry.getValue())));

        Map<String, Object> additionalProperties =
                additionalPropertiesMapperRegistry.map(
                        activeStoreConfiguration.getType().getName(), activeStoreConfiguration);

        return new ActiveStoreConfigurationResponse(
                activeStoreConfiguration.getNodeId(),
                currentItemHash,
                activeStoreConfiguration.getType().getName(),
                additionalProperties,
                features);
    }
}
