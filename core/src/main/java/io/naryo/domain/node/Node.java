package io.naryo.domain.node;

import java.util.Objects;
import java.util.UUID;

import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"id"})
public abstract class Node {

    protected final UUID id;
    private final NodeType type;
    protected NodeName name;
    protected SubscriptionConfiguration subscriptionConfiguration;
    protected InteractionConfiguration interactionConfiguration;
    protected NodeConnection connection;

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

    public abstract boolean supportsContractAddressInBloom();

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

    public Node merge(Node other) {
        if (other == null) {
            return this;
        }
        if (!this.id.equals(other.id)) {
            throw new IllegalArgumentException("Cannot merge nodes with different IDs");
        }
        if (other.name != null && !other.name.equals(this.name)) {
            this.name = other.name;
        }
        if (other.connection != null && !other.connection.equals(this.connection)) {
            this.connection = other.connection;
        }
        if (other.subscriptionConfiguration != null
                && !other.subscriptionConfiguration.equals(this.subscriptionConfiguration)) {
            this.subscriptionConfiguration = other.subscriptionConfiguration;
        }
        if (other.interactionConfiguration != null
                && !other.interactionConfiguration.equals(this.interactionConfiguration)) {
            this.interactionConfiguration = other.interactionConfiguration;
        }
        return this;
    }
}
