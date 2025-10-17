package io.naryo.api.node.common.eth.priv;

import io.naryo.api.node.common.eth.EthereumNodeRequestBuilder;
import io.naryo.api.node.common.request.PrivateEthereumNodeRequest;
import org.instancio.Instancio;

public final class PrivateEthereumNodeRequestBuilder
        extends EthereumNodeRequestBuilder<
                PrivateEthereumNodeRequestBuilder, PrivateEthereumNodeRequest> {

    private String groupId;
    private String precompiledAddress;

    @Override
    public PrivateEthereumNodeRequestBuilder self() {
        return this;
    }

    @Override
    public PrivateEthereumNodeRequest build() {
        return new PrivateEthereumNodeRequest(
                getName(),
                getSubscriptionConfiguration(),
                getInteractionConfiguration(),
                getConnection(),
                getGroupId(),
                getPrecompiledAddress());
    }

    public PrivateEthereumNodeRequestBuilder withGroupId(String groupId) {
        this.groupId = groupId;
        return self();
    }

    public String getGroupId() {
        return this.groupId == null ? Instancio.create(String.class) : this.groupId;
    }

    public PrivateEthereumNodeRequestBuilder withPrecompiledAddress(String precompiledAddress) {
        this.precompiledAddress = precompiledAddress;
        return self();
    }

    public String getPrecompiledAddress() {
        return this.precompiledAddress == null
                ? Instancio.create(String.class)
                : this.precompiledAddress;
    }
}
