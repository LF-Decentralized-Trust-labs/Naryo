package io.naryo.infrastructure.configuration.persistence.document.store;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.configuration.store.inactive.InactiveStoreConfiguration;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "stores_configuration")
@Setter
public abstract class StoreConfigurationPropertiesDocument implements StoreConfigurationDescriptor {
    private @MongoId String nodeId;

    public StoreConfigurationPropertiesDocument(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public UUID getNodeId() {
        return UUID.fromString(this.nodeId);
    }

    public static StoreConfigurationPropertiesDocument fromDomain(StoreConfiguration source) {
        return switch (source) {
            case ActiveStoreConfiguration active ->
                    new ActiveStoreConfigurationPropertiesDocument(
                            active.getNodeId().toString(),
                            active.getType().getName(),
                            active.getFeatures().entrySet().stream()
                                    .collect(
                                            Collectors.toMap(
                                                    Map.Entry::getKey,
                                                    e ->
                                                            StoreFeatureConfigurationPropertiesDocument
                                                                    .fromDomain(e.getValue()))),
                            new HashMap<>());
            case InactiveStoreConfiguration inactive ->
                    new InactiveStoreConfigurationPropertiesDocument(
                            inactive.getNodeId().toString());
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported store type: " + source.getClass());
        };
    }
}
