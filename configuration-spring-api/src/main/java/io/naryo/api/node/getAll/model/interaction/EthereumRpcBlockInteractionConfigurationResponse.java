package io.naryo.api.node.getAll.model.interaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Ethereum interaction configuration")
@Getter
public final class EthereumRpcBlockInteractionConfigurationResponse
        extends BlockInteractionConfigurationResponse {

    public EthereumRpcBlockInteractionConfigurationResponse() {}

    public static EthereumRpcBlockInteractionConfigurationResponse fromDomain() {
        return new EthereumRpcBlockInteractionConfigurationResponse();
    }
}
