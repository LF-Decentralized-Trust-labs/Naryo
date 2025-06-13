package io.naryo.domain.node.ethereum;

import java.util.UUID;

import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.NodeType;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class EthereumNode extends Node {

    private final EthereumNodeVisibility visibility;

    protected EthereumNode(
            UUID id,
            NodeName name,
            SubscriptionConfiguration subscriptionConfiguration,
            InteractionConfiguration interactionConfiguration,
            NodeConnection connection,
            EthereumNodeVisibility visibility) {
        super(
                id,
                name,
                NodeType.ETHEREUM,
                subscriptionConfiguration,
                interactionConfiguration,
                connection);
        this.visibility = visibility;
    }
}
