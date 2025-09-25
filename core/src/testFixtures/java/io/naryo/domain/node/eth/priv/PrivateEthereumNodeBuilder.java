package io.naryo.domain.node.eth.priv;

import io.naryo.domain.node.eth.EthereumNodeBuilder;
import io.naryo.domain.node.ethereum.priv.GroupId;
import io.naryo.domain.node.ethereum.priv.PrecompiledAddress;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import org.instancio.Instancio;

public final class PrivateEthereumNodeBuilder
        extends EthereumNodeBuilder<PrivateEthereumNodeBuilder, PrivateEthereumNode> {

    private GroupId groupId;
    private PrecompiledAddress precompiledAddress;

    @Override
    public PrivateEthereumNodeBuilder self() {
        return this;
    }

    @Override
    public PrivateEthereumNode build() {
        return new PrivateEthereumNode(
                getId(),
                getName(),
                getSubscriptionConfiguration(),
                getInteractionConfiguration(),
                getConnection(),
                getGroupId(),
                getPrecompiledAddress());
    }

    public PrivateEthereumNodeBuilder withGroupId(GroupId groupId) {
        this.groupId = groupId;
        return self();
    }

    public GroupId getGroupId() {
        return this.groupId == null ? Instancio.create(GroupId.class) : this.groupId;
    }

    public PrivateEthereumNodeBuilder withPrecompiledAddress(
            PrecompiledAddress precompiledAddress) {
        this.precompiledAddress = precompiledAddress;
        return self();
    }

    public PrecompiledAddress getPrecompiledAddress() {
        return this.precompiledAddress == null
                ? Instancio.create(PrecompiledAddress.class)
                : this.precompiledAddress;
    }
}
