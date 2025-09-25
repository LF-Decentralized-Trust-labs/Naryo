package io.naryo.domain.node.eth;

import io.naryo.domain.node.NodeBuilder;
import io.naryo.domain.node.ethereum.EthereumNode;

public abstract class EthereumNodeBuilder<
                T extends EthereumNodeBuilder<T, Y>, Y extends EthereumNode>
        extends NodeBuilder<T, Y> {}
