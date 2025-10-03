package io.naryo.infrastructure.configuration.persistence.document.broadcaster.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.mongodb.lang.Nullable;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterCacheConfigurationDescriptor;
import io.naryo.application.configuration.source.model.broadcaster.configuration.BroadcasterConfigurationDescriptor;
import io.naryo.domain.broadcaster.BroadcasterType;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "broadcasters_configuration")
@AllArgsConstructor
public final class BroadcasterConfigurationDocument implements BroadcasterConfigurationDescriptor {

    private final @MongoId String id;

    private final @NotNull @NotBlank String type;

    private @Nullable @Valid BroadcasterCacheConfiguration cache;

    private @Nullable Map<String, Object> additionalProperties;

    @Override
    public UUID getId() {
        return UUID.fromString(this.id);
    }

    @Override
    public BroadcasterType getType() {
        return this.type::toLowerCase;
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
    public void setCache(BroadcasterCacheConfigurationDescriptor cache) {
        this.cache = new BroadcasterCacheConfiguration(cache.getExpirationTime());
    }

    @Override
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public static BroadcasterConfigurationDocument fromDomain(BroadcasterConfiguration source) {
        return new BroadcasterConfigurationDocument(
                source.getId().toString(),
                source.getType().getName(),
                BroadcasterCacheConfiguration.fromDomain(source.getCache()),
                new HashMap<>());
    }
}
