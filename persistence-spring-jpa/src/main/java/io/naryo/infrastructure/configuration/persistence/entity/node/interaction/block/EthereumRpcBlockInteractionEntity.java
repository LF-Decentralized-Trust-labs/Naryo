package io.naryo.infrastructure.configuration.persistence.entity.node.interaction.block;

import io.naryo.application.configuration.source.model.node.interaction.EthereumRpcBlockInteractionDescriptor;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ethereum")
public class EthereumRpcBlockInteractionEntity extends BlockInteractionEntity
        implements EthereumRpcBlockInteractionDescriptor {}
