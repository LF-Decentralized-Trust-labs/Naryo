package io.naryo.application.configuration.source.model.node;

public interface PrivateEthereumNodeDescriptor extends EthereumNodeDescriptor {

    String getGroupId();

    String getPrecompiledAddress();

    void setGroupId(String groupId);

    void setPrecompiledAddress(String precompiledAddress);

    @Override
    default NodeDescriptor merge(NodeDescriptor descriptor) {
        var result = EthereumNodeDescriptor.super.merge(descriptor);

        if (descriptor instanceof PrivateEthereumNodeDescriptor other) {
            if (!this.getGroupId().equals(other.getGroupId())) {
                this.setGroupId(other.getGroupId());
            }
            if (!this.getPrecompiledAddress().equals(other.getPrecompiledAddress())) {
                this.setPrecompiledAddress(other.getPrecompiledAddress());
            }
        }

        return result;
    }
}
