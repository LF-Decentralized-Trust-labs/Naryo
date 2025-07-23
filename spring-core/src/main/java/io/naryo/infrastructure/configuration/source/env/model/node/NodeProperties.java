package io.naryo.infrastructure.configuration.source.env.model.node;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.node.NodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.HttpNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.WsNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.EthereumRpcBlockInteractionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.HederaMirrorNodeBlockInteractionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.NodeType;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.http.HttpConnectionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.connection.ws.WsConnectionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.interaction.block.EthereumRpcBlockInteractionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.interaction.block.HederaMirrorNodeBlockInteractionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.block.PollBlockSubscriptionProperties;
import io.naryo.infrastructure.configuration.source.env.model.node.subscription.block.PubsubBlockSubscriptionProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

public abstract class NodeProperties implements NodeDescriptor {

    private final @Getter @NotNull UUID id;
    private @Setter @Nullable String name;
    private @Nullable SubscriptionDescriptor subscription;
    private @Nullable InteractionDescriptor interaction;
    private @Nullable NodeConnectionDescriptor connection;

    protected NodeProperties(
            UUID id,
            String name,
            SubscriptionDescriptor subscription,
            InteractionDescriptor interaction,
            NodeConnectionDescriptor connection) {
        this.id = id;
        this.name = name;
        this.subscription = subscription;
        this.interaction = interaction;
        this.connection = connection;
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public Optional<SubscriptionDescriptor> getSubscription() {
        return Optional.ofNullable(subscription);
    }

    @Override
    public void setSubscription(SubscriptionDescriptor subscription) {
        switch (subscription) {
            case PubsubBlockSubscriptionProperties pubsub ->
                    this.subscription =
                            new PubsubBlockSubscriptionProperties(
                                    valueOrNull(pubsub.getInitialBlock()),
                                    valueOrNull(pubsub.getConfirmationBlocks()),
                                    valueOrNull(pubsub.getMissingTxRetryBlocks()),
                                    valueOrNull(pubsub.getEventInvalidationBlockThreshold()),
                                    valueOrNull(pubsub.getReplayBlockOffset()),
                                    valueOrNull(pubsub.getSyncBlockLimit()));
            case PollBlockSubscriptionProperties poll ->
                    this.subscription =
                            new PollBlockSubscriptionProperties(
                                    valueOrNull(poll.getInitialBlock()),
                                    valueOrNull(poll.getConfirmationBlocks()),
                                    valueOrNull(poll.getMissingTxRetryBlocks()),
                                    valueOrNull(poll.getEventInvalidationBlockThreshold()),
                                    valueOrNull(poll.getReplayBlockOffset()),
                                    valueOrNull(poll.getSyncBlockLimit()),
                                    valueOrNull(poll.getInterval()));
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported subscription type: "
                                    + subscription.getClass().getSimpleName());
        }
    }

    @Override
    public Optional<InteractionDescriptor> getInteraction() {
        return Optional.ofNullable(interaction);
    }

    @Override
    public void setInteraction(InteractionDescriptor interaction) {
        switch (interaction) {
            case EthereumRpcBlockInteractionDescriptor ignored ->
                    this.interaction = new EthereumRpcBlockInteractionProperties();
            case HederaMirrorNodeBlockInteractionDescriptor hedera ->
                    this.interaction =
                            new HederaMirrorNodeBlockInteractionProperties(
                                    valueOrNull(hedera.getLimitPerRequest()),
                                    valueOrNull(hedera.getRetriesPerRequest()));
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported interaction type: "
                                    + interaction.getClass().getSimpleName());
        }
    }

    @Override
    public Optional<NodeConnectionDescriptor> getConnection() {
        return Optional.ofNullable(connection);
    }

    @Override
    public void setConnection(NodeConnectionDescriptor connection) {
        switch (connection) {
            case HttpNodeConnectionDescriptor http ->
                    this.connection =
                            new HttpConnectionProperties(
                                    valueOrNull(http.getRetry()),
                                    valueOrNull(http.getEndpoint()),
                                    valueOrNull(http.getMaxIdleConnections()),
                                    valueOrNull(http.getKeepAliveDuration()),
                                    valueOrNull(http.getConnectionTimeout()),
                                    valueOrNull(http.getReadTimeout()));
            case WsNodeConnectionDescriptor ws ->
                    this.connection =
                            new WsConnectionProperties(
                                    valueOrNull(ws.getRetry()), valueOrNull(ws.getEndpoint()));
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported connection type: "
                                    + connection.getClass().getSimpleName());
        }
    }
}
