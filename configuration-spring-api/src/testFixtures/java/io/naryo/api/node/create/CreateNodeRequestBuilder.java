package io.naryo.api.node.create;

import java.util.Random;

import io.naryo.api.RequestBuilder;
import io.naryo.api.node.common.NodeRequestBuilder;
import io.naryo.api.node.common.eth.priv.PrivateEthereumNodeRequestBuilder;
import io.naryo.api.node.common.eth.pub.PublicEthereumNodeRequestBuilder;
import io.naryo.api.node.common.hedera.HederaNodeRequestBuilder;
import io.naryo.api.node.common.request.NodeRequest;
import io.naryo.api.node.create.model.CreateNodeRequest;

public final class CreateNodeRequestBuilder
        implements RequestBuilder<CreateNodeRequestBuilder, CreateNodeRequest> {

    private NodeRequest node;

    @Override
    public CreateNodeRequestBuilder self() {
        return this;
    }

    @Override
    public CreateNodeRequest build() {
        return new CreateNodeRequest(getNode());
    }

    public CreateNodeRequestBuilder withNode(NodeRequest node) {
        this.node = node;
        return self();
    }

    public NodeRequest getNode() {
        return this.node == null ? this.buildRandomNodeRequest() : this.node;
    }

    private NodeRequest buildRandomNodeRequest() {
        var random = new Random().nextInt(3);
        NodeRequestBuilder<?, ?> builder =
                switch (random) {
                    case 0 -> new PublicEthereumNodeRequestBuilder();
                    case 1 -> new PrivateEthereumNodeRequestBuilder();
                    case 2 -> new HederaNodeRequestBuilder();
                    default -> throw new IllegalStateException("Unexpected value: " + random);
                };

        return builder.build();
    }
}
