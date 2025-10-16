package io.naryo.api.node.create.eth;

import io.naryo.api.node.create.CreateNodeRequestBuilder;
import io.naryo.api.node.create.model.CreateEthereumNodeRequest;

public abstract class CreateEthereumNodeRequestBuilder<
                T extends CreateEthereumNodeRequestBuilder<T, Y>,
                Y extends CreateEthereumNodeRequest>
        extends CreateNodeRequestBuilder<T, Y> {}
