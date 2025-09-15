package io.naryo.infrastructure.configuration.persistence.document.store;

import java.util.Map;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("active_store")
public class ActiveStoreConfigurationPropertiesDocument extends StoreConfigurationPropertiesDocument
        implements ActiveStoreConfigurationDescriptor {

    private final String type;
    private Map<StoreFeatureType, StoreFeatureConfigurationPropertiesDocument> features;
    private Map<String, Object> additionalProperties;

    public ActiveStoreConfigurationPropertiesDocument(
            String nodeId,
            String type,
            Map<StoreFeatureType, StoreFeatureConfigurationPropertiesDocument> features,
            Map<String, Object> additionalProperties) {
        super(nodeId);
        this.type = type;
        this.features = features;
        this.additionalProperties = additionalProperties;
    }

    @Override
    public StoreType getType() {
        return this.type::toLowerCase;
    }

    @Override
    public Map<StoreFeatureType, StoreFeatureConfigurationPropertiesDocument> getFeatures() {
        return features;
    }

    @Override
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties == null ? Map.of() : this.additionalProperties;
    }

    @Override
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @Override
    public void setFeatures(
            Map<StoreFeatureType, ? extends StoreFeatureConfigurationDescriptor> features) {
        if (features.entrySet().stream()
                .allMatch(
                        t -> t.getValue() instanceof StoreFeatureConfigurationPropertiesDocument)) {
            this.features =
                    features.entrySet().stream()
                            .collect(
                                    Collectors.toMap(
                                            Map.Entry::getKey,
                                            entry ->
                                                    (StoreFeatureConfigurationPropertiesDocument)
                                                            entry.getValue()));
        } else {
            throw new IllegalArgumentException("Unsupported feature type: " + features.getClass());
        }
    }
}
