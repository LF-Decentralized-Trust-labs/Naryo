package io.naryo.infrastructure.configuration.persistence.document.node;

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
import io.naryo.infrastructure.configuration.persistence.document.node.connection.ConnectionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.http.HttpConnectionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.connection.ws.WsConnectionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.InteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.block.EthereumRpcBlockInteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.interaction.block.HederaMirrorNodeBlockInteractionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.SubscriptionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.block.PollBlockSubscriptionPropertiesDocument;
import io.naryo.infrastructure.configuration.persistence.document.node.subscription.block.PubSubBlockSubscriptionPropertiesDocument;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

@Document(collection = "nodes")
@Getter
public abstract class NodePropertiesDocument implements NodeDescriptor {

    private final @MongoId String id;
    private @Setter @Nullable String name;
    private @Nullable SubscriptionPropertiesDocument subscription;
    private @Nullable InteractionPropertiesDocument interaction;
    private @Nullable ConnectionPropertiesDocument connection;

    protected NodePropertiesDocument(
            String id,
            String name,
            SubscriptionPropertiesDocument subscription,
            InteractionPropertiesDocument interaction,
            ConnectionPropertiesDocument connection) {
        this.id = id;
        this.name = name;
        this.subscription = subscription;
        this.interaction = interaction;
        this.connection = connection;
    }

    @Override
    public UUID getId() {
        return UUID.fromString(this.id);
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
            case PubSubBlockSubscriptionPropertiesDocument pubsub ->
                    this.subscription =
                            new PubSubBlockSubscriptionPropertiesDocument(
                                    valueOrNull(pubsub.getInitialBlock()),
                                    valueOrNull(pubsub.getConfirmationBlocks()),
                                    valueOrNull(pubsub.getMissingTxRetryBlocks()),
                                    valueOrNull(pubsub.getEventInvalidationBlockThreshold()),
                                    valueOrNull(pubsub.getReplayBlockOffset()),
                                    valueOrNull(pubsub.getSyncBlockLimit()));
            case PollBlockSubscriptionPropertiesDocument poll ->
                    this.subscription =
                            new PollBlockSubscriptionPropertiesDocument(
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
                    this.interaction = new EthereumRpcBlockInteractionPropertiesDocument();
            case HederaMirrorNodeBlockInteractionDescriptor hedera ->
                    this.interaction =
                            new HederaMirrorNodeBlockInteractionPropertiesDocument(
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
                            new HttpConnectionPropertiesDocument(
                                    valueOrNull(http.getRetry()),
                                    valueOrNull(http.getEndpoint()),
                                    valueOrNull(http.getMaxIdleConnections()),
                                    valueOrNull(http.getKeepAliveDuration()),
                                    valueOrNull(http.getConnectionTimeout()),
                                    valueOrNull(http.getReadTimeout()));
            case WsNodeConnectionDescriptor ws ->
                    this.connection =
                            new WsConnectionPropertiesDocument(
                                    valueOrNull(ws.getRetry()), valueOrNull(ws.getEndpoint()));
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported connection type: "
                                    + connection.getClass().getSimpleName());
        }
    }
}
