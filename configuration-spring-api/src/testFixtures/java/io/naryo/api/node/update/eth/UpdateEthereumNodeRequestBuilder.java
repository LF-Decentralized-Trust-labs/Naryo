package io.naryo.api.node.update.eth;

import io.naryo.api.node.update.UpdateNodeRequestBuilder;
import io.naryo.api.node.update.model.UpdateEthereumNodeRequest;

public abstract class UpdateEthereumNodeRequestBuilder<
                T extends UpdateEthereumNodeRequestBuilder<T, Y>,
                Y extends UpdateEthereumNodeRequest>
        extends UpdateNodeRequestBuilder<T, Y> {}
