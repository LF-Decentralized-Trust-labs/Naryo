package io.naryo.infrastructure.configuration.persistence.entity.node.eth;

import io.naryo.application.configuration.source.model.node.PrivateEthereumNodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Optional;

@Entity
@DiscriminatorValue("private_ethereum")
public class PrivateEthereumNodeEntity extends EthereumNodeEntity implements PrivateEthereumNodeDescriptor {



    @Override
    public Optional<String> getGroupId() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getPrecompiledAddress() {
        return Optional.empty();
    }

    @Override
    public void setGroupId(String groupId) {

    }

    @Override
    public void setPrecompiledAddress(String precompiledAddress) {

    }


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
