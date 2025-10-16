package io.naryo.api.storeconfiguration.common.request;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "state")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ActiveStoreConfigurationRequest.class, name = "ACTIVE"),
    @JsonSubTypes.Type(value = InactiveStoreConfigurationRequest.class, name = "INACTIVE"),
})
@AllArgsConstructor
@EqualsAndHashCode
public abstract sealed class StoreConfigurationRequest implements StoreConfigurationDescriptor
        permits ActiveStoreConfigurationRequest, InactiveStoreConfigurationRequest {

    @NotNull private UUID nodeId;

    @Override
    public UUID getNodeId() {
        return nodeId;
    }
}
