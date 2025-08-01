package io.naryo.infrastructure.configuration.persistence.entity.node.eth;

import io.naryo.application.configuration.source.model.node.PublicEthereumNodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Optional;


@Entity
@DiscriminatorValue("public_ethereum")
public class PublicEthereumnodeEntity extends EthereumNodeEntity implements PublicEthereumNodeDescriptor {


    @Override
    public void setName(String name) {

    }

    @Override
    public void setInteraction(InteractionDescriptor interaction) {

    }

    @Override
    public void setConnection(NodeConnectionDescriptor connection) {

    }
}
