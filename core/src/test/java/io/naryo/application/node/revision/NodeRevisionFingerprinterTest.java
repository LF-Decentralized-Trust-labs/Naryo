package io.naryo.application.node.revision;

import java.util.Random;

import io.naryo.application.common.revision.BaseRevisionFingerprinterTest;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeBuilder;
import io.naryo.domain.node.eth.priv.PrivateEthereumNodeBuilder;
import io.naryo.domain.node.eth.pub.PublicEthereumNodeBuilder;
import io.naryo.domain.node.hedera.HederaNodeBuilder;
import org.junit.jupiter.api.BeforeEach;

class NodeRevisionFingerprinterTest extends BaseRevisionFingerprinterTest<Node> {

    protected NodeRevisionFingerprinterTest() {
        super(Node::getId);
    }

    @BeforeEach
    void setUp() {
        fingerprinter = new NodeRevisionFingerprinter();
    }

    @Override
    protected Node createInput() {
        var random = new Random().nextInt(3);
        NodeBuilder<?, ?> builder =
                switch (random) {
                    case 0 -> new PublicEthereumNodeBuilder();
                    case 1 -> new PrivateEthereumNodeBuilder();
                    case 2 -> new HederaNodeBuilder();
                    default -> throw new IllegalStateException("Unexpected value: " + random);
                };

        return builder.build();
    }
}
