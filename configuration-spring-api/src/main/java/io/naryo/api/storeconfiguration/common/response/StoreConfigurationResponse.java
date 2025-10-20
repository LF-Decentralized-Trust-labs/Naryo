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

    //    public static StoreConfigurationResponse map(
    //            StoreConfiguration storeConfiguration, String currentItemHash) {
    //        return switch (storeConfiguration) {
    //            case ActiveStoreConfiguration active ->
    //                    ActiveStoreConfigurationResponse.map(active, currentItemHash);
    //            case InactiveStoreConfiguration inactive ->
    //                    InactiveStoreConfigurationResponse.map(inactive, currentItemHash);
    //            default -> throw new IllegalStateException("Unexpected value: " +
    // storeConfiguration);
    //        };
    //    }
}
