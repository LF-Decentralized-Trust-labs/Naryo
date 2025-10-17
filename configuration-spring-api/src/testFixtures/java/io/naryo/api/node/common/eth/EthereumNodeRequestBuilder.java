package io.naryo.api.node.common.eth;

import io.naryo.api.node.common.NodeRequestBuilder;
import io.naryo.api.node.common.request.EthereumNodeRequest;

public abstract class EthereumNodeRequestBuilder<
                T extends EthereumNodeRequestBuilder<T, Y>, Y extends EthereumNodeRequest>
        extends NodeRequestBuilder<T, Y> {}
