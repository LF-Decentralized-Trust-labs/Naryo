package io.naryo.api.node.common.response.interaction;

import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;

public sealed interface InteractionConfigurationResponse
        permits EthereumRpcBlockInteractionConfigurationResponse,
                HederaMirrorNodeBlockInteractionConfigurationResponse {

    static InteractionConfigurationResponse fromDomain(
            InteractionConfiguration interactionConfiguration) {
        return switch (interactionConfiguration) {
            case HederaMirrorNodeBlockInteractionConfiguration hedera ->
                    HederaMirrorNodeBlockInteractionConfigurationResponse.builder()
                            .strategy(hedera.getStrategy().name())
                            .mode(hedera.getMode().name())
                            .limitPerRequest(hedera.getLimitPerRequest().value())
                            .retriesPerRequest(hedera.getRetriesPerRequest().value())
                            .build();
            case EthereumRpcBlockInteractionConfiguration ethereum ->
                    EthereumRpcBlockInteractionConfigurationResponse.builder()
                            .strategy(ethereum.getStrategy().name())
                            .mode(ethereum.getMode().name())
                            .build();
            default ->
                    throw new IllegalStateException(
                            "Unexpected value: " + interactionConfiguration);
        };
    }
}
