package io.naryo.api.node.common.response.interaction;

import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;

public abstract class InteractionConfigurationResponse {

    private final String strategy;
    private final String mode;

    public InteractionConfigurationResponse(String strategy, String mode) {
        this.strategy = strategy;
        this.mode = mode;
    }

    public static InteractionConfigurationResponse fromDomain(
            InteractionConfiguration interactionConfiguration) {
        return switch (interactionConfiguration) {
            case HederaMirrorNodeBlockInteractionConfiguration hedera ->
                    new HederaMirrorNodeBlockInteractionConfigurationResponse(
                            hedera.getStrategy().name(),
                            hedera.getMode().name(),
                            hedera.getLimitPerRequest().value(),
                            hedera.getRetriesPerRequest().value());
            case EthereumRpcBlockInteractionConfiguration ethereum ->
                    new EthereumRpcBlockInteractionConfigurationResponse(
                            ethereum.getStrategy().name(), ethereum.getMode().name());
            default ->
                    throw new IllegalStateException(
                            "Unexpected value: " + interactionConfiguration);
        };
    }
}
