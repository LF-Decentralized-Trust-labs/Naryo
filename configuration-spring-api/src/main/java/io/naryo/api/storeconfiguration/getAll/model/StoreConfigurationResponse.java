package io.naryo.api.storeconfiguration.getAll.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "state")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ActiveStoreConfigurationResponse.class, name = "ACTIVE"),
    @JsonSubTypes.Type(value = InactiveStoreConfigurationResponse.class, name = "INACTIVE"),
})
@Schema(
        description = "Base class for store configuration request",
        discriminatorProperty = "state",
        discriminatorMapping = {
            @DiscriminatorMapping(
                    value = "ACTIVE",
                    schema = ActiveStoreConfigurationResponse.class),
            @DiscriminatorMapping(
                    value = "INACTIVE",
                    schema = InactiveStoreConfigurationResponse.class)
        })
@EqualsAndHashCode
@Getter
public abstract class StoreConfigurationResponse {

    private final UUID nodeId;
    private final String currentItemHash;

    public StoreConfigurationResponse(UUID nodeId, String currentItemHash) {
        this.nodeId = nodeId;
        this.currentItemHash = currentItemHash;
    }
}
