package io.naryo.api.node.common.response;

import io.naryo.api.node.common.response.connection.NodeConnectionResponse;
import io.naryo.api.node.common.response.interaction.InteractionConfigurationResponse;
import io.naryo.api.node.common.response.subscription.SubscriptionConfigurationResponse;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.ethereum.EthereumNode;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;

public sealed interface NodeResponse
        permits HederaNodeResponse, PrivateEthereumNodeResponse, PublicEthereumNodeResponse {

    static NodeResponse fromDomain(Node node) {
        return switch (node.getType()) {
            case ETHEREUM -> {
                var ethereumNode = (EthereumNode) node;
                yield switch (ethereumNode.getVisibility()) {
                    case PUBLIC ->
                            PublicEthereumNodeResponse.builder()
                                    .id(node.getId())
                                    .type(node.getType().name())
                                    .name(node.getName().value())
                                    .subscriptionConfiguration(
                                            SubscriptionConfigurationResponse.fromDomain(
                                                    node.getSubscriptionConfiguration()))
                                    .interactionConfiguration(
                                            InteractionConfigurationResponse.fromDomain(
                                                    node.getInteractionConfiguration()))
                                    .connection(
                                            NodeConnectionResponse.fromDomain(node.getConnection()))
                                    .visibility(ethereumNode.getVisibility().name())
                                    .build();
                    case PRIVATE -> {
                        var privNode = (PrivateEthereumNode) ethereumNode;
                        yield PrivateEthereumNodeResponse.builder()
                                .id(node.getId())
                                .type(node.getType().name())
                                .name(node.getName().value())
                                .subscriptionConfiguration(
                                        SubscriptionConfigurationResponse.fromDomain(
                                                node.getSubscriptionConfiguration()))
                                .interactionConfiguration(
                                        InteractionConfigurationResponse.fromDomain(
                                                node.getInteractionConfiguration()))
                                .connection(NodeConnectionResponse.fromDomain(node.getConnection()))
                                .visibility(ethereumNode.getVisibility().name())
                                .groupId(privNode.getGroupId().value())
                                .precompiledAddress(privNode.getPrecompiledAddress().value())
                                .build();
                    }
                };
            }
            case HEDERA ->
                    HederaNodeResponse.builder()
                            .id(node.getId())
                            .type(node.getType().name())
                            .name(node.getName().value())
                            .subscriptionConfiguration(
                                    SubscriptionConfigurationResponse.fromDomain(
                                            node.getSubscriptionConfiguration()))
                            .interactionConfiguration(
                                    InteractionConfigurationResponse.fromDomain(
                                            node.getInteractionConfiguration()))
                            .connection(NodeConnectionResponse.fromDomain(node.getConnection()))
                            .build();
        };
    }
}
