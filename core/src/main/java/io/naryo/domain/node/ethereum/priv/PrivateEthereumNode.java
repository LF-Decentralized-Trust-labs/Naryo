package io.naryo.domain.node.ethereum.priv;

import java.util.Objects;
import java.util.UUID;

import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.ethereum.EthereumNode;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class PrivateEthereumNode extends EthereumNode {

    private final GroupId groupId;
    private final PrecompiledAddress precompiledAddress;

    public PrivateEthereumNode(
            UUID id,
            NodeName name,
            SubscriptionConfiguration subscriptionConfiguration,
            InteractionConfiguration interactionConfiguration,
            NodeConnection connection,
            GroupId groupId,
            PrecompiledAddress precompiledAddress) {
        super(
                id,
                name,
                subscriptionConfiguration,
                interactionConfiguration,
                connection,
                EthereumNodeVisibility.PRIVATE);
        Objects.requireNonNull(groupId, "GroupId cannot be null");
        Objects.requireNonNull(precompiledAddress, "PrecompiledAddress cannot be null");
        this.groupId = groupId;
        this.precompiledAddress = precompiledAddress;
    }
}
