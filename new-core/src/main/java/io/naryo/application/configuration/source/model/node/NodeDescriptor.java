package io.naryo.application.configuration.source.model.node;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.NodeType;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface NodeDescriptor extends MergeableDescriptor<NodeDescriptor> {

    UUID getId();

    Optional<String> getName();

    Optional<NodeType> getType();

    Optional<SubscriptionDescriptor> getSubscription();

    Optional<InteractionDescriptor> getInteraction();

    Optional<NodeConnectionDescriptor> getConnection();

    void setName(String name);

    void setType(NodeType type);

    void setSubscription(SubscriptionDescriptor subscription);

    void setInteraction(InteractionDescriptor interaction);

    void setConnection(NodeConnectionDescriptor connection);

    @Override
    default NodeDescriptor merge(NodeDescriptor descriptor) {
        if (descriptor == null) {
            return this;
        }

        mergeOptionals(this::setName, this.getName(), descriptor.getName());
        mergeOptionals(this::setType, this.getType(), descriptor.getType());

        descriptor
                .getSubscription()
                .ifPresent(
                        newSubscription -> {
                            this.getSubscription()
                                    .ifPresentOrElse(
                                            currentSubscription -> {
                                                if (!newSubscription
                                                        .getClass()
                                                        .equals(currentSubscription.getClass())) {
                                                    this.setSubscription(newSubscription);
                                                } else {
                                                    this.setSubscription(
                                                            currentSubscription.merge(
                                                                    newSubscription));
                                                }
                                            },
                                            () -> this.setSubscription(newSubscription));
                        });

        descriptor
                .getInteraction()
                .ifPresent(
                        newInteraction -> {
                            this.getInteraction()
                                    .ifPresentOrElse(
                                            currentInteraction -> {
                                                currentInteraction.merge(newInteraction);
                                            },
                                            () -> this.setInteraction(newInteraction));
                        });

        descriptor
                .getConnection()
                .ifPresent(
                        newConnection -> {
                            this.getConnection()
                                    .ifPresentOrElse(
                                            currentConnection -> {
                                                currentConnection.merge(newConnection);
                                            },
                                            () -> this.setConnection(newConnection));
                        });

        return this;
    }
}
