package io.naryo.infrastructure.configuration.persistence.document.node.interaction;

import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.ethereum.EthereumRpcBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.block.EthereumRpcBlockInteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.block.HederaMirrorNodeBlockInteractionPropertiesDocument;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public abstract class InteractionPropertiesDocument implements InteractionDescriptor {
    public static InteractionPropertiesDocument fromDomain(InteractionConfiguration source) {
        return switch (source) {
            case EthereumRpcBlockInteractionConfiguration ignore ->
                    new EthereumRpcBlockInteractionPropertiesDocument();
            case HederaMirrorNodeBlockInteractionConfiguration hedera ->
                    new HederaMirrorNodeBlockInteractionPropertiesDocument(
                            hedera.getLimitPerRequest().value(),
                            hedera.getRetriesPerRequest().value());
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported interaction type: " + source.getClass().getSimpleName());
        };
    }
}
