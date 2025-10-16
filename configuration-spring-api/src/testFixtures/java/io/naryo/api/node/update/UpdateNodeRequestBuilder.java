package io.naryo.api.node.update;

import java.util.UUID;

import io.naryo.api.node.RequestBuilder;
import io.naryo.api.node.common.connection.HttpNodeConnectionRequestBuilder;
import io.naryo.api.node.common.interaction.block.EthereumRpcBlockInteractionConfigurationRequestBuilder;
import io.naryo.api.node.common.request.connection.NodeConnectionRequest;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;
import io.naryo.api.node.common.request.subscription.SubscriptionConfigurationRequest;
import io.naryo.api.node.common.subscription.BlockSubscriptionConfigurationRequestBuilder;
import io.naryo.api.node.update.model.UpdateNodeRequest;
import org.instancio.Instancio;

public abstract class UpdateNodeRequestBuilder<
                T extends UpdateNodeRequestBuilder<T, Y>, Y extends UpdateNodeRequest>
        implements RequestBuilder<T, Y> {

    private UUID id;
    private String name;
    private SubscriptionConfigurationRequest subscriptionConfiguration;
    private InteractionConfigurationRequest interactionConfiguration;
    private NodeConnectionRequest connection;
    private String prevItemHash;

    public UpdateNodeRequestBuilder<T, Y> withId(UUID id) {
        this.id = id;
        return self();
    }

    public UUID getId() {
        return this.id == null ? UUID.randomUUID() : this.id;
    }

    public UpdateNodeRequestBuilder<T, Y> withName(String name) {
        this.name = name;
        return self();
    }

    public String getName() {
        return this.name == null ? Instancio.create(String.class) : this.name;
    }

    public UpdateNodeRequestBuilder<T, Y> withSubscriptionConfiguration(
            SubscriptionConfigurationRequest subscriptionConfiguration) {
        this.subscriptionConfiguration = subscriptionConfiguration;
        return self();
    }

    public SubscriptionConfigurationRequest getSubscriptionConfiguration() {
        return this.subscriptionConfiguration == null
                ? new BlockSubscriptionConfigurationRequestBuilder().build()
                : this.subscriptionConfiguration;
    }

    public UpdateNodeRequestBuilder<T, Y> withInteractionConfiguration(
            InteractionConfigurationRequest interactionConfiguration) {
        this.interactionConfiguration = interactionConfiguration;
        return self();
    }

    public InteractionConfigurationRequest getInteractionConfiguration() {
        return this.interactionConfiguration == null
                ? new EthereumRpcBlockInteractionConfigurationRequestBuilder().build()
                : this.interactionConfiguration;
    }

    public UpdateNodeRequestBuilder<T, Y> withConnection(NodeConnectionRequest connection) {
        this.connection = connection;
        return self();
    }

    public NodeConnectionRequest getConnection() {
        return this.connection == null
                ? new HttpNodeConnectionRequestBuilder().build()
                : this.connection;
    }

    public UpdateNodeRequestBuilder<T, Y> withPrevItemHash(String prevItemHash) {
        this.prevItemHash = prevItemHash;
        return self();
    }

    public String getPrevItemHash() {
        return this.prevItemHash == null ? Instancio.create(String.class) : this.prevItemHash;
    }
}
