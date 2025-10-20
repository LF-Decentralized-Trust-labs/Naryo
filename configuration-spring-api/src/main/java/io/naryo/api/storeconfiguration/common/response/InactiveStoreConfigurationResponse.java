package io.naryo.api.storeconfiguration.common.response;

import java.util.UUID;

import io.naryo.domain.configuration.store.inactive.InactiveStoreConfiguration;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public final class InactiveStoreConfigurationResponse extends StoreConfigurationResponse {

    public InactiveStoreConfigurationResponse(UUID nodeId, String currentItemHash) {
        super(nodeId, currentItemHash);
    }

    public static InactiveStoreConfigurationResponse map(
            InactiveStoreConfiguration inactiveStoreConfiguration, String currentItemHash) {
        return new InactiveStoreConfigurationResponse(
                inactiveStoreConfiguration.getNodeId(), currentItemHash);
    }
}
