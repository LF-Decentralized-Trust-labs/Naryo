package io.naryo.api.node.update;

import java.util.Random;

import io.naryo.api.RequestBuilder;
import io.naryo.api.node.common.NodeRequestBuilder;
import io.naryo.api.node.common.eth.priv.PrivateEthereumNodeRequestBuilder;
import io.naryo.api.node.common.eth.pub.PublicEthereumNodeRequestBuilder;
import io.naryo.api.node.common.hedera.HederaNodeRequestBuilder;
import io.naryo.api.node.common.request.NodeRequest;
import io.naryo.api.node.update.model.UpdateNodeRequest;
import org.instancio.Instancio;

public final class UpdateNodeRequestBuilder
        implements RequestBuilder<UpdateNodeRequestBuilder, UpdateNodeRequest> {

    private NodeRequest node;
    private String prevItemHash;

    @Override
    public UpdateNodeRequestBuilder self() {
        return this;
    }

    @Override
    public UpdateNodeRequest build() {
        return new UpdateNodeRequest(getNode(), getPrevItemHash());
    }

    public UpdateNodeRequestBuilder withNode(NodeRequest node) {
        this.node = node;
        return self();
    }

    public NodeRequest getNode() {
        return this.node == null ? this.buildRandomNodeRequest() : this.node;
    }

    public UpdateNodeRequestBuilder withPrevItemHash(String prevItemHash) {
        this.prevItemHash = prevItemHash;
        return self();
    }

    public String getPrevItemHash() {
        return this.prevItemHash == null ? Instancio.create(String.class) : this.prevItemHash;
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
