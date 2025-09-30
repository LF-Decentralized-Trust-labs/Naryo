package io.naryo.infrastructure.configuration.persistence.entity.node;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.node.NodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.HttpNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.connection.WsNodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.EthereumRpcBlockInteractionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.HederaMirrorNodeBlockInteractionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.PollBlockSubscriptionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.PubsubBlockSubscriptionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.ethereum.EthereumNode;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.ConnectionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.http.HttpConnectionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.ws.WsConnectionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.eth.PrivateEthereumNodeEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.eth.PublicEthereumNodeEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.hedera.HederaNodeEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.interaction.InteractionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.interaction.block.EthereumRpcBlockInteractionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.interaction.block.HederaMirrorNodeBlockInteractionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.subscription.SubscriptionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.subscription.block.PollBlockSubscriptionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.subscription.block.PubSubBlockSubscriptionEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static io.naryo.application.common.util.OptionalUtil.valueOrNull;

@Entity
@Table(name = "node")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "node_type")
@NoArgsConstructor
public abstract class NodeEntity implements NodeDescriptor {

    private @Id @Column(name = "id") UUID id;

    private @Nullable @Column(name = "name") String name;

    private @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) @JoinColumn(
            name = "subscription_id") @Nullable SubscriptionEntity subscription;

    private @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) @JoinColumn(
            name = "interaction_id") @Nullable InteractionEntity interaction;

    private @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) @JoinColumn(
            name = "connection_id") @Nullable ConnectionEntity connection;

    protected NodeEntity(
            UUID id,
            @Nullable String name,
            @Nullable SubscriptionEntity subscription,
            @Nullable InteractionEntity interaction,
            @Nullable ConnectionEntity connection) {
        this.id = id;
        this.name = name;
        this.subscription = subscription;
        this.interaction = interaction;
        this.connection = connection;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Optional<SubscriptionDescriptor> getSubscription() {
        return Optional.ofNullable(this.subscription);
    }

    @Override
    public void setSubscription(SubscriptionDescriptor subscription) {
        switch (subscription) {
            case PubsubBlockSubscriptionDescriptor pubsub ->
                    this.subscription =
                            new PubSubBlockSubscriptionEntity(
                                    valueOrNull(pubsub.getInitialBlock()),
                                    valueOrNull(pubsub.getConfirmationBlocks()),
                                    valueOrNull(pubsub.getMissingTxRetryBlocks()),
                                    valueOrNull(pubsub.getEventInvalidationBlockThreshold()),
                                    valueOrNull(pubsub.getReplayBlockOffset()),
                                    valueOrNull(pubsub.getSyncBlockLimit()));
            case PollBlockSubscriptionDescriptor poll ->
                    this.subscription =
                            new PollBlockSubscriptionEntity(
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
    public Optional<InteractionEntity> getInteraction() {
        return Optional.ofNullable(this.interaction);
    }

    @Override
    public void setInteraction(InteractionDescriptor interaction) {
        switch (interaction) {
            case EthereumRpcBlockInteractionDescriptor ethereum ->
                    this.interaction = new EthereumRpcBlockInteractionEntity();
            case HederaMirrorNodeBlockInteractionDescriptor hedera ->
                    this.interaction =
                            new HederaMirrorNodeBlockInteractionEntity(
                                    valueOrNull(hedera.getLimitPerRequest()),
                                    valueOrNull(hedera.getRetriesPerRequest()));
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported interaction type: "
                                    + interaction.getClass().getSimpleName());
        }
    }

    @Override
    public Optional<ConnectionEntity> getConnection() {
        return Optional.ofNullable(this.connection);
    }

    @Override
    public void setConnection(NodeConnectionDescriptor connection) {
        switch (connection) {
            case HttpNodeConnectionDescriptor http ->
                    this.connection =
                            new HttpConnectionEntity(
                                    valueOrNull(http.getRetry()),
                                    valueOrNull(http.getEndpoint()),
                                    valueOrNull(http.getMaxIdleConnections()),
                                    valueOrNull(http.getKeepAliveDuration()),
                                    valueOrNull(http.getConnectionTimeout()),
                                    valueOrNull(http.getReadTimeout()));
            case WsNodeConnectionDescriptor ws ->
                    this.connection =
                            new WsConnectionEntity(
                                    valueOrNull(ws.getRetry()), valueOrNull(ws.getEndpoint()));
            default ->
                    throw new IllegalArgumentException(
                            "Unsupported connection type: "
                                    + connection.getClass().getSimpleName());
        }
    }

    public static NodeEntity fromDomain(Node source) {
        NodeEntity nodeEntity = null;

        String name = source.getName().value();
        SubscriptionEntity subscriptionEntity =
                SubscriptionEntity.fromDomain(source.getSubscriptionConfiguration());
        InteractionEntity interactionEntity =
                InteractionEntity.fromDomain(source.getInteractionConfiguration());
        ConnectionEntity connectionEntity = ConnectionEntity.fromDomain(source.getConnection());

        switch (source.getType()) {
            case HEDERA ->
                    nodeEntity =
                            new HederaNodeEntity(
                                    source.getId(),
                                    name,
                                    subscriptionEntity,
                                    interactionEntity,
                                    connectionEntity);
            case ETHEREUM -> {
                var ethSource = (EthereumNode) source;
                switch (ethSource.getVisibility()) {
                    case PRIVATE -> {
                        var privateEthSource = (PrivateEthereumNode) ethSource;
                        nodeEntity =
                                new PrivateEthereumNodeEntity(
                                        source.getId(),
                                        name,
                                        subscriptionEntity,
                                        interactionEntity,
                                        connectionEntity,
                                        privateEthSource.getGroupId().value(),
                                        privateEthSource.getPrecompiledAddress().value());
                    }
                    case PUBLIC ->
                            nodeEntity =
                                    new PublicEthereumNodeEntity(
                                            source.getId(),
                                            name,
                                            subscriptionEntity,
                                            interactionEntity,
                                            connectionEntity);
                    default -> throw new IllegalArgumentException("Invalid Ethereum visibility");
                }
            }
        }

        return nodeEntity;
    }
}
