package io.naryo.infrastructure.configuration.provider.node.mapper;

import java.util.Optional;

import io.naryo.domain.node.Node;
import io.naryo.domain.node.NodeName;
import io.naryo.domain.node.ethereum.EthereumNodeVisibility;
import io.naryo.domain.node.ethereum.priv.GroupId;
import io.naryo.domain.node.ethereum.priv.PrecompiledAddress;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import io.naryo.domain.node.ethereum.pub.PublicEthereumNode;
import io.naryo.domain.node.hedera.HederaNode;
import io.naryo.infrastructure.configuration.persistence.document.node.NodePropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.eth.EthereumNodePropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.eth.EthereumNodeVisibilityConfigurationDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.eth.PrivateEthereumNodeVisibilityDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.hedera.HederaNodePropertiesDocument;

public abstract class NodePropertiesDocumentMapper {

    public static Node fromDocument(NodePropertiesDocument document) {
        if (document instanceof EthereumNodePropertiesDocument) {
            return mapEthereumNode((EthereumNodePropertiesDocument) document);
        } else if (document instanceof HederaNodePropertiesDocument) {
            return mapHederaNode((HederaNodePropertiesDocument) document);
        }
        throw new IllegalArgumentException("Unsupported document type: " + document.getClass());
    }

    private static Node mapEthereumNode(EthereumNodePropertiesDocument document) {
        var visibility =
                Optional.ofNullable(document.getConfiguration())
                        .map(EthereumNodeVisibilityConfigurationDocument::getVisibility)
                        .orElse(EthereumNodeVisibility.PUBLIC);

        return switch (visibility) {
            case PRIVATE -> mapPrivateEthereumNode(document);
            case PUBLIC -> mapPublicEthereumNode(document);
        };
    }

    private static PrivateEthereumNode mapPrivateEthereumNode(
            EthereumNodePropertiesDocument document) {
        var privateCfg = (PrivateEthereumNodeVisibilityDocument) document.getConfiguration();
        return new PrivateEthereumNode(
                document.getId(),
                new NodeName(document.getName()),
                SubscriptionPropertiesDocumentMapper.fromDocument(document.getSubscription()),
                InteractionPropertiesDocumentMapper.fromDocument(document.getInteraction()),
                ConnectionPropertiesDocumentMapper.fromDocument(document.getConnection()),
                new GroupId(
                        Optional.ofNullable(privateCfg)
                                .map(PrivateEthereumNodeVisibilityDocument::getGroupId)
                                .orElse("default")),
                new PrecompiledAddress(
                        Optional.ofNullable(privateCfg)
                                .map(PrivateEthereumNodeVisibilityDocument::getPrecompiledAddress)
                                .orElse("0x0")));
    }

    private static PublicEthereumNode mapPublicEthereumNode(
            EthereumNodePropertiesDocument document) {
        return new PublicEthereumNode(
                document.getId(),
                new NodeName(document.getName()),
                SubscriptionPropertiesDocumentMapper.fromDocument(document.getSubscription()),
                InteractionPropertiesDocumentMapper.fromDocument(document.getInteraction()),
                ConnectionPropertiesDocumentMapper.fromDocument(document.getConnection()));
    }

    private static Node mapHederaNode(HederaNodePropertiesDocument document) {
        return new HederaNode(
                document.getId(),
                new NodeName(document.getName()),
                SubscriptionPropertiesDocumentMapper.fromDocument(document.getSubscription()),
                InteractionPropertiesDocumentMapper.fromDocument(document.getInteraction()),
                ConnectionPropertiesDocumentMapper.fromDocument(document.getConnection()));
    }
}
