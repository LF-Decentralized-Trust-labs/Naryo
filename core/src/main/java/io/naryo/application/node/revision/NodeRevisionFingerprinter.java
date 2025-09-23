package io.naryo.application.node.revision;

import io.naryo.application.common.revision.DefaultRevisionFingerprinter;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeNormalizer;

public final class NodeRevisionFingerprinter extends DefaultRevisionFingerprinter<Node> {

    public NodeRevisionFingerprinter() {
        super(NodeNormalizer.INSTANCE);
    }

    public NodeRevisionFingerprinter(NodeNormalizer normalizer) {
        super(normalizer);
    }
}
