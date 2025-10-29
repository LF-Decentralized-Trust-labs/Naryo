package io.naryo.api.broadcasterconfiguration.get;

import java.util.*;
import java.util.stream.Collectors;

import io.naryo.api.broadcasterconfiguration.get.model.BroadcasterConfigurationResponse;
import io.naryo.application.broadcaster.configuration.mapper.BroadcasterConfigurationAdditionalPropertiesMapperRegistry;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public final class BroadcasterConfigurationResponseMapper {
    private final BroadcasterConfigurationAdditionalPropertiesMapperRegistry
            additionalPropertiesMapperRegistry;

    public List<BroadcasterConfigurationResponse> map(
            Collection<BroadcasterConfiguration> broadcasterConfigurations,
            Map<UUID, String> fingerprints) {
        return broadcasterConfigurations.stream()
                .map(
                        broadcasterConfiguration -> {
                            String currentItemHash =
                                    fingerprints.get(broadcasterConfiguration.getId());
                            return map(broadcasterConfiguration, currentItemHash);
                        })
                .collect(Collectors.toList());
    }

    public BroadcasterConfigurationResponse map(
            BroadcasterConfiguration broadcasterConfiguration, String currentItemHash) {
        Map<String, Object> additionalProperties =
                additionalPropertiesMapperRegistry.map(
                        broadcasterConfiguration.getType().getName(), broadcasterConfiguration);

        return BroadcasterConfigurationResponse.fromDomain(
                broadcasterConfiguration, additionalProperties, currentItemHash);
    }
}
