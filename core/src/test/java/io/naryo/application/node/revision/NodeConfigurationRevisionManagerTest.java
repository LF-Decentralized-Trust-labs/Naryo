package io.naryo.application.node.revision;

import java.util.Random;

import io.naryo.application.configuration.revision.manager.BaseConfigurationRevisionManagerTest;
import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManager;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.node.configuration.manager.NodeConfigurationManager;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeBuilder;
import io.naryo.domain.node.eth.priv.PrivateEthereumNodeBuilder;
import io.naryo.domain.node.eth.pub.PublicEthereumNodeBuilder;
import io.naryo.domain.node.hedera.HederaNodeBuilder;

import static org.junit.jupiter.api.Assertions.*;

class NodeConfigurationRevisionManagerTest
        extends BaseConfigurationRevisionManagerTest<
                Node, NodeConfigurationManager, NodeRevisionFingerprinter> {

    protected NodeConfigurationRevisionManagerTest() {
        super(Node::getId, NodeConfigurationManager.class);
    }

    @Override
    protected ConfigurationRevisionManager<Node> createManager(
            NodeConfigurationManager configurationManager,
            NodeRevisionFingerprinter fingerprinter,
            LiveRegistry<Node> liveRegistry) {
        return new NodeConfigurationRevisionManager(
                configurationManager, fingerprinter, liveRegistry);
    }

    @Override
    protected NodeRevisionFingerprinter createFingerprinter() {
        return new NodeRevisionFingerprinter();
    }

    @Override
    protected Node newItem() {
        return newBuilder().build();
    }

    @Override
    protected Node updatedVariantOf(Node base) {
        return newBuilder().withId(base.getId()).build();
    }

    private NodeBuilder<?, ?> newBuilder() {
        var random = new Random().nextInt(3);
        return switch (random) {
            case 0 -> new PublicEthereumNodeBuilder();
            case 1 -> new PrivateEthereumNodeBuilder();
            case 2 -> new HederaNodeBuilder();
            default -> throw new IllegalStateException("Unexpected value: " + random);
        };
    }
}
