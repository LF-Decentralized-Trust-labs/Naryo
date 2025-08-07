package io.naryo.infrastructure.configuration.source.env.model.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

@Setter
public class ActiveStoreConfigurationProperties extends StoreConfigurationProperties
        implements ActiveStoreConfigurationDescriptor {

    private final @NotNull StoreType type;
    private @NotNull Map<StoreFeatureType, StoreFeatureConfigurationProperties> features;
    private Map<String, Object> additionalProperties;
    private @Nullable ConfigurationSchema propertiesSchema;

    public ActiveStoreConfigurationProperties(
            @NotNull UUID nodeId,
            @NotNull StoreType type,
            @NotNull Map<StoreFeatureType, StoreFeatureConfigurationProperties> features,
            Map<String, Object> additionalProperties,
            @Nullable ConfigurationSchema propertiesSchema) {
        super(nodeId);
        this.type = type;
        this.features = features;
        this.additionalProperties =
                additionalProperties == null ? new HashMap<>() : additionalProperties;
        this.propertiesSchema = propertiesSchema;
    }

    @Override
    public StoreType getType() {
        return type;
    }

    @Override
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @Override
    public Optional<ConfigurationSchema> getPropertiesSchema() {
        return Optional.ofNullable(propertiesSchema);
    }

    @Override
    public Map<StoreFeatureType, StoreFeatureConfigurationProperties> getFeatures() {
        return features;
    }

    @Override
    public void setFeatures(
            Map<StoreFeatureType, ? extends StoreFeatureConfigurationDescriptor> features) {
        if (features.entrySet().stream()
                .allMatch(t -> t.getValue() instanceof StoreFeatureConfigurationProperties)) {
            this.features =
                    features.entrySet().stream()
                            .collect(
                                    Collectors.toMap(
                                            Map.Entry::getKey,
                                            entry ->
                                                    (StoreFeatureConfigurationProperties)
                                                            entry.getValue()));
        } else {
            throw new IllegalArgumentException("Unsupported feature type: " + features.getClass());
        }
    }
}
