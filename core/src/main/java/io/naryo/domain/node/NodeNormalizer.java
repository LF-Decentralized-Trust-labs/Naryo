package io.naryo.domain.node;

import io.naryo.domain.common.normalization.NormalizationUtil;
import io.naryo.domain.node.connection.NodeConnectionNormalizer;
import io.naryo.domain.node.ethereum.EthereumNode;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import io.naryo.domain.node.ethereum.pub.PublicEthereumNode;
import io.naryo.domain.node.hedera.HederaNode;
import io.naryo.domain.normalization.Normalizer;

public final class NodeNormalizer implements Normalizer<Node> {

    public static final NodeNormalizer INSTANCE = new NodeNormalizer();

    private final NodeConnectionNormalizer nodeConnectionNormalizer;

    public NodeNormalizer() {
        this(NodeConnectionNormalizer.INSTANCE);
    }

    public NodeNormalizer(NodeConnectionNormalizer nodeConnectionNormalizer) {
        this.nodeConnectionNormalizer = nodeConnectionNormalizer;
    }

    @Override
    public Node normalize(Node in) {
        if (in == null) {
            return null;
        }

        return switch (in) {
            case EthereumNode ethIn ->
                    switch (ethIn) {
                        case PublicEthereumNode pubEthIn ->
                                pubEthIn.toBuilder()
                                        .name(normalize(pubEthIn.name))
                                        .connection(
                                                nodeConnectionNormalizer.normalize(
                                                        pubEthIn.getConnection()))
                                        .build();
                        case PrivateEthereumNode privEthIn ->
                                privEthIn.toBuilder()
                                        .name(normalize(privEthIn.name))
                                        .connection(
                                                nodeConnectionNormalizer.normalize(
                                                        privEthIn.getConnection()))
                                        .build();
                        default -> throw new IllegalStateException("Unexpected value: " + ethIn);
                    };

            case HederaNode hedIn ->
                    hedIn.toBuilder()
                            .name(normalize(hedIn.name))
                            .connection(nodeConnectionNormalizer.normalize(hedIn.getConnection()))
                            .build();

            default -> throw new IllegalStateException("Unexpected value: " + in);
        };
    }

    private NodeName normalize(NodeName in) {
        if (in == null || in.value() == null) {
            return null;
        }
        return new NodeName(NormalizationUtil.normalize(in.value()));
    }
}
