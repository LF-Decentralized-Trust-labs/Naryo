package io.naryo.application.configuration.source.model.node;

import java.util.Optional;

import io.naryo.domain.node.ethereum.EthereumNodeVisibility;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface PrivateEthereumNodeDescriptor extends EthereumNodeDescriptor {

    Optional<String> getGroupId();

    Optional<String> getPrecompiledAddress();

    void setGroupId(String groupId);

    void setPrecompiledAddress(String precompiledAddress);

    @Override
    default EthereumNodeVisibility getVisibility() {
        return EthereumNodeVisibility.PRIVATE;
    }

    @Override
    default NodeDescriptor merge(NodeDescriptor descriptor) {
        var result = EthereumNodeDescriptor.super.merge(descriptor);

        if (descriptor instanceof PrivateEthereumNodeDescriptor other) {
            mergeOptionals(this::setGroupId, this.getGroupId(), other.getGroupId());
            mergeOptionals(
                    this::setPrecompiledAddress,
                    this.getPrecompiledAddress(),
                    other.getPrecompiledAddress());
        }

        return result;
    }
}
