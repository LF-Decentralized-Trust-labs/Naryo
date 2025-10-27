package io.naryo.api.storeconfiguration.common.request;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.application.configuration.source.model.store.StoreConfigurationDescriptor;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "state")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ActiveStoreConfigurationRequest.class, name = "ACTIVE"),
    @JsonSubTypes.Type(value = InactiveStoreConfigurationRequest.class, name = "INACTIVE"),
})
@Schema(
    description = "Base class for store configuration request",
    discriminatorProperty = "state",
    discriminatorMapping = {
        @DiscriminatorMapping(
            value = "ACTIVE",
            schema = ActiveStoreConfigurationRequest.class),
        @DiscriminatorMapping(
            value = "INACTIVE",
            schema = InactiveStoreConfigurationRequest.class)
    })
@AllArgsConstructor
@EqualsAndHashCode
public abstract class StoreConfigurationRequest implements StoreConfigurationDescriptor {

    @NotNull private UUID nodeId;

    @Override
    public UUID getNodeId() {
        return nodeId;
    }
}
