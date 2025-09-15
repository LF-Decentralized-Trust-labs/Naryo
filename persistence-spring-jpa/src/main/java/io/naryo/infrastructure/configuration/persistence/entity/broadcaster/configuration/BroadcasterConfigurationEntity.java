package io.naryo.infrastructure.configuration.persistence.entity.broadcaster.configuration;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.definition.ConfigurationSchema;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.domain.broadcaster.BroadcasterType;
import io.naryo.infrastructure.configuration.persistence.entity.common.schema.ConfigurationSchemaEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import static io.naryo.infrastructure.configuration.persistence.entity.common.schema.ConfigurationSchemaEntity.fromEntity;
import static io.naryo.infrastructure.configuration.persistence.entity.common.schema.ConfigurationSchemaEntity.toEntity;

@Entity
@Table(name = "broadcaster_configuration")
@AllArgsConstructor
@NoArgsConstructor
public final class BroadcasterConfigurationEntity implements BroadcasterConfigurationDescriptor {

    private @Column(name = "id") @Id UUID id;

    private @Column(name = "broadcaster_type") @NotNull @NotBlank String type;

    private @Embedded @Valid BroadcasterCacheEntity cache;

    private @Column(name = "additional_properties") @JdbcTypeCode(SqlTypes.JSON) Map<String, Object>
            additionalProperties;

    private @Embedded ConfigurationSchemaEntity propertiesSchema;

    @Override
    public void setCache(BroadcasterCacheConfigurationDescriptor cache) {
        this.cache = new BroadcasterCacheEntity(cache.getExpirationTime());
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
    public UUID getId() {
        return this.id;
    }

    @Override
    public BroadcasterType getType() {
        return () -> this.type.toLowerCase();
    }

    @Override
    public Optional<BroadcasterCacheConfigurationDescriptor> getCache() {
        return Optional.ofNullable(this.cache);
    }

    @Override
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties == null ? Map.of() : this.additionalProperties;
    }

    @Override
    public Optional<ConfigurationSchema> getPropertiesSchema() {
        return Optional.ofNullable(fromEntity(this.propertiesSchema));
    }
}
