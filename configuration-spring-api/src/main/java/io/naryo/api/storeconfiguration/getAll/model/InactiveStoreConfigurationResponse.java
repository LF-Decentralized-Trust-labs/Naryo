package io.naryo.api.storeconfiguration.getAll.model;

import java.util.UUID;

import io.naryo.domain.configuration.store.inactive.InactiveStoreConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;

@Schema(description = "Inactive store configuration")
@EqualsAndHashCode(callSuper = true)
public final class InactiveStoreConfigurationResponse extends StoreConfigurationResponse {

    public InactiveStoreConfigurationResponse(UUID nodeId, String currentItemHash) {
        super(nodeId, currentItemHash);
    }

    public static InactiveStoreConfigurationResponse fromDomain(
            InactiveStoreConfiguration inactiveStoreConfiguration, String currentItemHash) {
        return new InactiveStoreConfigurationResponse(
                inactiveStoreConfiguration.getNodeId(), currentItemHash);
    }
}
