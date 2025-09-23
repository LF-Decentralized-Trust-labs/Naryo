package io.naryo.domain.node;

import java.util.UUID;

import io.naryo.domain.DomainBuilder;
import io.naryo.domain.node.connection.HttpNodeConnectionBuilder;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.EthereumRpcBlockInteractionConfigurationBuilder;
import io.naryo.domain.node.subscription.BlockSubscriptionConfigurationBuilder;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import org.instancio.Instancio;

public abstract class NodeBuilder<T extends NodeBuilder<T, Y>, Y extends Node>
        implements DomainBuilder<T, Y> {

    private UUID id;
    private NodeName name;
    private SubscriptionConfiguration subscriptionConfiguration;
    private InteractionConfiguration interactionConfiguration;
    private NodeConnection connection;

    public NodeBuilder<T, Y> withId(UUID id) {
        this.id = id;
        return self();
    }

    public UUID getId() {
        return this.id == null ? UUID.randomUUID() : this.id;
    }

    public NodeBuilder<T, Y> withName(NodeName name) {
        this.name = name;
        return self();
    }

    public NodeName getName() {
        return this.name == null ? Instancio.create(NodeName.class) : this.name;
    }

    public NodeBuilder<T, Y> withSubscriptionConfiguration(
            SubscriptionConfiguration subscriptionConfiguration) {
        this.subscriptionConfiguration = subscriptionConfiguration;
        return self();
    }

    public SubscriptionConfiguration getSubscriptionConfiguration() {
        return this.subscriptionConfiguration == null
                ? new BlockSubscriptionConfigurationBuilder().build()
                : this.subscriptionConfiguration;
    }

    public NodeBuilder<T, Y> withInteractionConfiguration(
            InteractionConfiguration interactionConfiguration) {
        this.interactionConfiguration = interactionConfiguration;
        return self();
    }

    public InteractionConfiguration getInteractionConfiguration() {
        return this.interactionConfiguration == null
                ? new EthereumRpcBlockInteractionConfigurationBuilder().build()
                : this.interactionConfiguration;
    }

    public NodeBuilder<T, Y> withConnection(NodeConnection connection) {
        this.connection = connection;
        return self();
    }

    public NodeConnection getConnection() {
        return this.connection == null ? new HttpNodeConnectionBuilder().build() : this.connection;
    }
}
