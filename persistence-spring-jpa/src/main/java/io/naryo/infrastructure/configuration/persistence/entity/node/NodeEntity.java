package io.naryo.infrastructure.configuration.persistence.entity.node;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.node.EthereumNodeDescriptor;
import io.naryo.application.configuration.source.model.node.NodeDescriptor;
import io.naryo.application.configuration.source.model.node.PrivateEthereumNodeDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.infrastructure.configuration.persistence.entity.node.connection.ConnectionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.eth.PrivateEthereumNodeEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.eth.PublicEthereumNodeEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.hedera.HederaNodeEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.interaction.InteractionEntity;
import io.naryo.infrastructure.configuration.persistence.entity.node.subscription.SubscriptionEntity;
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
        this.subscription = SubscriptionEntity.fromDescriptor(subscription);
    }

    @Override
    public Optional<InteractionEntity> getInteraction() {
        return Optional.ofNullable(this.interaction);
    }

    @Override
    public void setInteraction(InteractionDescriptor interaction) {
        this.interaction = InteractionEntity.fromDescriptor(interaction);
    }

    @Override
    public Optional<ConnectionEntity> getConnection() {
        return Optional.ofNullable(this.connection);
    }

    @Override
    public void setConnection(NodeConnectionDescriptor connection) {
        this.connection = ConnectionEntity.fromDescriptor(connection);
    }

    public static NodeEntity fromDescriptor(NodeDescriptor descriptor) {
        NodeEntity nodeEntity = null;

        String name = valueOrNull(descriptor.getName());
        SubscriptionEntity subscriptionEntity =
                SubscriptionEntity.fromDescriptor(
                        valueOrNull(NodeDescriptor::getSubscription, descriptor));
        InteractionEntity interactionEntity =
                InteractionEntity.fromDescriptor(
                        valueOrNull(NodeDescriptor::getInteraction, descriptor));
        ConnectionEntity connectionEntity =
                ConnectionEntity.fromDescriptor(
                        valueOrNull(NodeDescriptor::getConnection, descriptor));

        switch (descriptor.getType()) {
            case HEDERA ->
                    nodeEntity =
                            new HederaNodeEntity(
                                    descriptor.getId(),
                                    name,
                                    subscriptionEntity,
                                    interactionEntity,
                                    connectionEntity);
            case ETHEREUM -> {
                var ethSource = (EthereumNodeDescriptor) descriptor;
                switch (ethSource.getVisibility()) {
                    case PRIVATE -> {
                        var privateEthSource = (PrivateEthereumNodeDescriptor) ethSource;
                        nodeEntity =
                                new PrivateEthereumNodeEntity(
                                        descriptor.getId(),
                                        name,
                                        subscriptionEntity,
                                        interactionEntity,
                                        connectionEntity,
                                        valueOrNull(privateEthSource.getGroupId()),
                                        valueOrNull(privateEthSource.getPrecompiledAddress()));
                    }
                    case PUBLIC ->
                            nodeEntity =
                                    new PublicEthereumNodeEntity(
                                            descriptor.getId(),
                                            name,
                                            subscriptionEntity,
                                            interactionEntity,
                                            connectionEntity);
                }
            }
        }
        return nodeEntity;
    }
}
