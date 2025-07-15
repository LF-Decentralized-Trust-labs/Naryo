package io.naryo.infrastructure.configuration.provider.node.mapper;

import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.BlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.LimitPerRequest;
import io.naryo.domain.node.interaction.block.hedera.RetriesPerRequest;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.InteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.block.BlockInteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.block.EthereumRpcBlockInteractionModePropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.block.HederaMirrorNodeBlockInteractionModePropertiesDocument;

public abstract class InteractionPropertiesDocumentMapper {

    public static InteractionConfiguration fromDocument(InteractionPropertiesDocument document) {
        if (document instanceof BlockInteractionPropertiesDocument) {
            return mapBlockInteraction((BlockInteractionPropertiesDocument) document);
        }
        return createDefaultInteractionConfiguration();
    }

    private static BlockInteractionConfiguration mapBlockInteraction(BlockInteractionPropertiesDocument document) {
        if (document.getConfiguration() instanceof EthereumRpcBlockInteractionModePropertiesDocument) {
            return mapEthereumRpcBlockInteraction((EthereumRpcBlockInteractionModePropertiesDocument) document.getConfiguration());
        } else if (document.getConfiguration() instanceof HederaMirrorNodeBlockInteractionModePropertiesDocument) {
            return mapHederaMirrorNodeBlockInteraction((HederaMirrorNodeBlockInteractionModePropertiesDocument) document.getConfiguration());
        }
        throw new IllegalArgumentException("Unsupported document type: " + document.getClass());
    }

    private static EthereumRpcBlockInteractionConfiguration mapEthereumRpcBlockInteraction(EthereumRpcBlockInteractionModePropertiesDocument document) {
        return new EthereumRpcBlockInteractionConfiguration();
    }

    private static HederaMirrorNodeBlockInteractionConfiguration mapHederaMirrorNodeBlockInteraction(HederaMirrorNodeBlockInteractionModePropertiesDocument document) {
        return new HederaMirrorNodeBlockInteractionConfiguration(
            new LimitPerRequest(document.getLimitPerRequest()),
            new RetriesPerRequest(document.getRetriesPerRequest())
        );
    }

    private static InteractionConfiguration createDefaultInteractionConfiguration() {
        return new EthereumRpcBlockInteractionConfiguration();
    }
}
