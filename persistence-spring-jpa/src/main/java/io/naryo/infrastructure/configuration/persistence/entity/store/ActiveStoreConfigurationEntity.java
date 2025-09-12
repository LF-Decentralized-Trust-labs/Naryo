package io.naryo.infrastructure.configuration.persistence.entity.store;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.store.ActiveStoreConfigurationDescriptor;
import io.naryo.application.configuration.source.model.store.StoreFeatureConfigurationDescriptor;
import io.naryo.domain.configuration.store.active.StoreType;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.infrastructure.configuration.persistence.entity.common.schema.ConfigurationSchemaEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import static io.naryo.infrastructure.configuration.persistence.entity.common.schema.ConfigurationSchemaEntity.fromEntity;
import static io.naryo.infrastructure.configuration.persistence.entity.common.schema.ConfigurationSchemaEntity.toEntity;

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
    private @Embedded ConfigurationSchemaEntity propertiesSchema;

    public ActiveStoreConfigurationEntity(
            UUID nodeId,
            String type,
            List<StoreFeatureConfigurationEntity> features,
            Map<String, Object> additionalProperties,
            ConfigurationSchemaEntity propertiesSchema) {
        super(nodeId);
        this.type = type;
        this.features = features;
        this.additionalProperties = additionalProperties;
        this.propertiesSchema = propertiesSchema;
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
    public Optional<ConfigurationSchema> getPropertiesSchema() {
        return Optional.ofNullable(fromEntity(this.propertiesSchema));
    }

    @Override
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @Override
    public void setPropertiesSchema(ConfigurationSchema propertiesSchema) {
        this.propertiesSchema = toEntity(propertiesSchema);
    }

    @Override
    public void setFeatures(
            Map<StoreFeatureType, ? extends StoreFeatureConfigurationDescriptor> features) {
        if (features.values().stream()
                .allMatch(f -> f instanceof StoreFeatureConfigurationEntity)) {
            this.features = (List<StoreFeatureConfigurationEntity>) features.values();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported feature type for JPA entity: " + features.getClass());
        }
    }
}
