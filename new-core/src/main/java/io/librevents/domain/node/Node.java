package io.librevents.domain.node;

import java.util.Objects;
import java.util.UUID;

import io.librevents.domain.node.connection.NodeConnection;
import io.librevents.domain.node.interaction.InteractionConfiguration;
import io.librevents.domain.node.subscription.SubscriptionConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"id"})
public abstract class Node {

    protected final UUID id;
    protected NodeName name;
    protected SubscriptionConfiguration subscriptionConfiguration;
    protected InteractionConfiguration interactionConfiguration;
    protected NodeConnection connection;
    private final NodeType type;

    public void reconfigure(
            NodeName name,
            SubscriptionConfiguration subscriptionConfiguration,
            InteractionConfiguration interactionConfiguration,
            NodeConnection connection) {
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(
                subscriptionConfiguration, "SubscriptionConfiguration cannot be null");
        Objects.requireNonNull(interactionConfiguration, "InteractionConfiguration cannot be null");
        Objects.requireNonNull(connection, "Connection cannot be null");

        this.name = name;
        this.subscriptionConfiguration = subscriptionConfiguration;
        this.interactionConfiguration = interactionConfiguration;
        this.connection = connection;
    }

    protected Node(
            UUID id,
            NodeName name,
            NodeType type,
            SubscriptionConfiguration subscriptionConfiguration,
            InteractionConfiguration interactionConfiguration,
            NodeConnection connection) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.subscriptionConfiguration = subscriptionConfiguration;
        this.interactionConfiguration = interactionConfiguration;
        this.connection = connection;

        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(type, "Type cannot be null");
        Objects.requireNonNull(
                subscriptionConfiguration, "SubscriptionConfiguration cannot be null");
        Objects.requireNonNull(interactionConfiguration, "InteractionConfiguration cannot be null");
        Objects.requireNonNull(connection, "Connection cannot be null");
    }
}
