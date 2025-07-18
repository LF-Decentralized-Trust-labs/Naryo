package io.naryo.infrastructure.configuration.provider.node.mapper;

import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.BlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.LimitPerRequest;
import io.naryo.domain.node.interaction.block.hedera.RetriesPerRequest;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.InteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.block.BlockInteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.block.EthereumRpcBlockInteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.block.HederaMirrorNodeBlockInteractionPropertiesDocument;

public abstract class InteractionPropertiesDocumentMapper {

    public static InteractionConfiguration fromDocument(InteractionPropertiesDocument document) {
        if (document instanceof BlockInteractionPropertiesDocument) {
            return mapBlockInteraction((BlockInteractionPropertiesDocument) document);
        }
        return createDefaultInteractionConfiguration();
    }

    private static BlockInteractionConfiguration mapBlockInteraction(
            BlockInteractionPropertiesDocument document) {
        if (document instanceof EthereumRpcBlockInteractionPropertiesDocument) {
            return mapEthereumRpcBlockInteraction(
                    (EthereumRpcBlockInteractionPropertiesDocument) document);
        } else if (document instanceof HederaMirrorNodeBlockInteractionPropertiesDocument) {
            return mapHederaMirrorNodeBlockInteraction(
                    (HederaMirrorNodeBlockInteractionPropertiesDocument) document);
        }
        throw new IllegalArgumentException("Unsupported document type: " + document.getClass());
    }

    private static EthereumRpcBlockInteractionConfiguration mapEthereumRpcBlockInteraction(
            EthereumRpcBlockInteractionPropertiesDocument document) {
        return new EthereumRpcBlockInteractionConfiguration();
    }

    private static HederaMirrorNodeBlockInteractionConfiguration
            mapHederaMirrorNodeBlockInteraction(
                    HederaMirrorNodeBlockInteractionPropertiesDocument document) {
        return new HederaMirrorNodeBlockInteractionConfiguration(
                new LimitPerRequest(document.getLimitPerRequest()),
                new RetriesPerRequest(document.getRetriesPerRequest()));
    }

    private static InteractionConfiguration createDefaultInteractionConfiguration() {
        return new EthereumRpcBlockInteractionConfiguration();
    }
}
