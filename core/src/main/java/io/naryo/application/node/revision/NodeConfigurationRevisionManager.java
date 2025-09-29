package io.naryo.application.node.revision;

import io.naryo.application.configuration.revision.manager.DefaultConfigurationRevisionManager;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.node.configuration.manager.NodeConfigurationManager;
import io.naryo.domain.node.Node;

public final class NodeConfigurationRevisionManager
        extends DefaultConfigurationRevisionManager<Node> {

    public NodeConfigurationRevisionManager(
            NodeConfigurationManager configurationManager,
            NodeRevisionFingerprinter fingerprinter,
            LiveRegistry<Node> live) {
        super(configurationManager, fingerprinter, Node::getId, live);
    }
}
