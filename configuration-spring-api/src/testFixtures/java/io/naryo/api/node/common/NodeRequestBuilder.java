package io.naryo.api.node.common;

import io.naryo.api.RequestBuilder;
import io.naryo.api.node.common.connection.HttpNodeConnectionRequestBuilder;
import io.naryo.api.node.common.interaction.block.EthereumRpcBlockInteractionConfigurationRequestBuilder;
import io.naryo.api.node.common.request.NodeRequest;
import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.api.node.common.subscription.BlockSubscriptionConfigurationRequestBuilder;
import org.instancio.Instancio;

public abstract class NodeRequestBuilder<T extends NodeRequestBuilder<T, Y>, Y extends NodeRequest>
        implements RequestBuilder<T, Y> {

    private String name;
    private SubscriptionConfigurationRequest subscriptionConfiguration;
    private InteractionConfigurationRequest interactionConfiguration;
    private NodeConnectionRequest connection;

    public NodeRequestBuilder<T, Y> withName(String name) {
        this.name = name;
        return self();
    }

    public String getName() {
        return this.name == null ? Instancio.create(String.class) : this.name;
    }

    public NodeRequestBuilder<T, Y> withSubscriptionConfiguration(
            SubscriptionConfigurationRequest subscriptionConfiguration) {
        this.subscriptionConfiguration = subscriptionConfiguration;
        return self();
    }

    public SubscriptionConfigurationRequest getSubscriptionConfiguration() {
        return this.subscriptionConfiguration == null
                ? new BlockSubscriptionConfigurationRequestBuilder().build()
                : this.subscriptionConfiguration;
    }

    public NodeRequestBuilder<T, Y> withInteractionConfiguration(
            InteractionConfigurationRequest interactionConfiguration) {
        this.interactionConfiguration = interactionConfiguration;
        return self();
    }

    public InteractionConfigurationRequest getInteractionConfiguration() {
        return this.interactionConfiguration == null
                ? new EthereumRpcBlockInteractionConfigurationRequestBuilder().build()
                : this.interactionConfiguration;
    }

    public NodeRequestBuilder<T, Y> withConnection(NodeConnectionRequest connection) {
        this.connection = connection;
        return self();
    }

    public NodeConnectionRequest getConnection() {
        return this.connection == null
                ? new HttpNodeConnectionRequestBuilder().build()
                : this.connection;
    }
}
