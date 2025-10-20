package io.naryo.api.storeconfiguration.common.response;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;

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
