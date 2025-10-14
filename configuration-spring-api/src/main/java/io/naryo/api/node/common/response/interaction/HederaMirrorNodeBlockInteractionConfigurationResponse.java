package io.naryo.api.node.common.response.interaction;

public class HederaMirrorNodeBlockInteractionConfigurationResponse
        extends InteractionConfigurationResponse {

    private final int limitPerRequest;
    private final int retriesPerRequest;

    public HederaMirrorNodeBlockInteractionConfigurationResponse(
            String strategy, String mode, int limitPerRequest, int retriesPerRequest) {
        super(strategy, mode);
        this.limitPerRequest = limitPerRequest;
        this.retriesPerRequest = retriesPerRequest;
    }
}
