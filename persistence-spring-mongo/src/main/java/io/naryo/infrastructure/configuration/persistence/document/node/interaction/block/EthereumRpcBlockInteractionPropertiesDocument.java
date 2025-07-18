package io.naryo.infrastructure.configuration.persistence.document.node.interaction.block;

import io.naryo.application.configuration.source.model.node.interaction.EthereumRpcBlockInteractionDescriptor;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("ethereum_rpc_block_interaction")
public final class EthereumRpcBlockInteractionPropertiesDocument
        extends BlockInteractionPropertiesDocument
        implements EthereumRpcBlockInteractionDescriptor {}
