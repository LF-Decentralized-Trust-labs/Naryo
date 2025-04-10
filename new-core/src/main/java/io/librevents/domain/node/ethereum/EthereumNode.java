package io.librevents.domain.node.ethereum;

import java.util.UUID;

import io.librevents.domain.node.Node;
import io.librevents.domain.node.NodeName;
import io.librevents.domain.node.NodeType;
import io.librevents.domain.node.connection.NodeConnection;
import io.librevents.domain.node.interaction.InteractionConfiguration;
import io.librevents.domain.node.subscription.SubscriptionConfiguration;
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
