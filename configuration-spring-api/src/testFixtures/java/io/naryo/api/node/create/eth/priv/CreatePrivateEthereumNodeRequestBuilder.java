package io.naryo.api.node.create.eth.priv;

import io.naryo.api.node.create.eth.CreateEthereumNodeRequestBuilder;
import io.naryo.api.node.create.model.CreatePrivateEthereumNodeRequest;
import org.instancio.Instancio;

public final class CreatePrivateEthereumNodeRequestBuilder
        extends CreateEthereumNodeRequestBuilder<
                CreatePrivateEthereumNodeRequestBuilder, CreatePrivateEthereumNodeRequest> {

    private String groupId;
    private String precompiledAddress;

    @Override
    public CreatePrivateEthereumNodeRequestBuilder self() {
        return this;
    }

    @Override
    public CreatePrivateEthereumNodeRequest build() {
        return new CreatePrivateEthereumNodeRequest(
                getName(),
                getSubscriptionConfiguration(),
                getInteractionConfiguration(),
                getConnection(),
                getGroupId(),
                getPrecompiledAddress());
    }

    public CreatePrivateEthereumNodeRequestBuilder withGroupId(String groupId) {
        this.groupId = groupId;
        return self();
    }

    public String getGroupId() {
        return this.groupId == null ? Instancio.create(String.class) : this.groupId;
    }

    public CreatePrivateEthereumNodeRequestBuilder withPrecompiledAddress(
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
