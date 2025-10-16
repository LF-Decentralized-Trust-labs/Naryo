package io.naryo.api.node.update.eth.priv;

import io.naryo.api.node.update.eth.UpdateEthereumNodeRequestBuilder;
import io.naryo.api.node.update.model.UpdatePrivateEthereumNodeRequest;
import org.instancio.Instancio;

public final class UpdatePrivateEthereumNodeRequestBuilder
        extends UpdateEthereumNodeRequestBuilder<
                UpdatePrivateEthereumNodeRequestBuilder, UpdatePrivateEthereumNodeRequest> {

    private String groupId;
    private String precompiledAddress;

    @Override
    public UpdatePrivateEthereumNodeRequestBuilder self() {
        return this;
    }

    @Override
    public UpdatePrivateEthereumNodeRequest build() {
        return new UpdatePrivateEthereumNodeRequest(
                getName(),
                getSubscriptionConfiguration(),
                getInteractionConfiguration(),
                getConnection(),
                getGroupId(),
                getPrecompiledAddress(),
                getPrevItemHash());
    }

    public UpdatePrivateEthereumNodeRequestBuilder withGroupId(String groupId) {
        this.groupId = groupId;
        return self();
    }

    public String getGroupId() {
        return this.groupId == null ? Instancio.create(String.class) : this.groupId;
    }

    public UpdatePrivateEthereumNodeRequestBuilder withPrecompiledAddress(
            String precompiledAddress) {
        this.precompiledAddress = precompiledAddress;
        return self();
    }

    public String getPrecompiledAddress() {
        return this.precompiledAddress == null
                ? Instancio.create(String.class)
                : this.precompiledAddress;
    }
}
