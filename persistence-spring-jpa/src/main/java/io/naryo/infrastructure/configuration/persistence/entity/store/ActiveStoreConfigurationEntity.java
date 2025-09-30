package io.naryo.infrastructure.configuration.persistence.entity.store;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@DiscriminatorValue("active")
@NoArgsConstructor
public class ActiveStoreConfigurationEntity extends StoreConfigurationEntity
        implements ActiveStoreConfigurationDescriptor {

    private @Column(name = "type") String type;
    private @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "store_configuration_id") List<StoreFeatureConfigurationEntity> features;
    private @Column(name = "additional_properties") @JdbcTypeCode(SqlTypes.JSON) Map<String, Object>
            additionalProperties;

    public ActiveStoreConfigurationEntity(
            UUID nodeId,
            String type,
            List<StoreFeatureConfigurationEntity> features,
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
    public Map<StoreFeatureType, ? extends StoreFeatureConfigurationDescriptor> getFeatures() {
        return this.features.stream()
                .collect(
                        Collectors.toMap(
                                StoreFeatureConfigurationEntity::getType, Function.identity()));
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
        this.features = StoreFeatureConfigurationEntity.flatFeaturesMap(features);
    }
}
