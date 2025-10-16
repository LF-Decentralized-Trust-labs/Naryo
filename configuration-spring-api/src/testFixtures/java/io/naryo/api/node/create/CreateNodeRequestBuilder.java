package io.naryo.api.node.create;

import io.naryo.api.RequestBuilder;
import io.naryo.api.node.common.connection.HttpNodeConnectionRequestBuilder;
import io.naryo.api.node.common.interaction.block.EthereumRpcBlockInteractionConfigurationRequestBuilder;
import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.api.node.common.subscription.BlockSubscriptionConfigurationRequestBuilder;
import io.naryo.api.node.create.model.CreateNodeRequest;
import org.instancio.Instancio;

public abstract class CreateNodeRequestBuilder<
                T extends CreateNodeRequestBuilder<T, Y>, Y extends CreateNodeRequest>
        implements RequestBuilder<T, Y> {

    private String name;
    private SubscriptionConfigurationRequest subscriptionConfiguration;
    private InteractionConfigurationRequest interactionConfiguration;
    private NodeConnectionRequest connection;

    public CreateNodeRequestBuilder<T, Y> withName(String name) {
        this.name = name;
        return self();
    }

    public String getName() {
        return this.name == null ? Instancio.create(String.class) : this.name;
    }

    public CreateNodeRequestBuilder<T, Y> withSubscriptionConfiguration(
            SubscriptionConfigurationRequest subscriptionConfiguration) {
        this.subscriptionConfiguration = subscriptionConfiguration;
        return self();
    }

    public SubscriptionConfigurationRequest getSubscriptionConfiguration() {
        return this.subscriptionConfiguration == null
                ? new BlockSubscriptionConfigurationRequestBuilder().build()
                : this.subscriptionConfiguration;
    }

    public CreateNodeRequestBuilder<T, Y> withInteractionConfiguration(
            InteractionConfigurationRequest interactionConfiguration) {
        this.interactionConfiguration = interactionConfiguration;
        return self();
    }

    public InteractionConfigurationRequest getInteractionConfiguration() {
        return this.interactionConfiguration == null
                ? new EthereumRpcBlockInteractionConfigurationRequestBuilder().build()
                : this.interactionConfiguration;
    }

    public CreateNodeRequestBuilder<T, Y> withConnection(NodeConnectionRequest connection) {
        this.connection = connection;
        return self();
    }

    public NodeConnectionRequest getConnection() {
        return this.connection == null
                ? new HttpNodeConnectionRequestBuilder().build()
                : this.connection;
    }
}
