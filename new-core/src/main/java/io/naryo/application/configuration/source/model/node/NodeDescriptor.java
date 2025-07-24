package io.naryo.application.configuration.source.model.node;

import java.util.Optional;
import java.util.UUID;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.NodeType;

import static io.naryo.application.common.util.MergeUtil.mergeDescriptors;
import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface NodeDescriptor extends MergeableDescriptor<NodeDescriptor> {

    UUID getId();

    Optional<String> getName();

    NodeType getType();

    <T extends SubscriptionDescriptor> Optional<T> getSubscription();

    <T extends InteractionDescriptor> Optional<T> getInteraction();

    <T extends NodeConnectionDescriptor> Optional<T> getConnection();

    void setName(String name);

    void setSubscription(SubscriptionDescriptor subscription);

    void setInteraction(InteractionDescriptor interaction);

    void setConnection(NodeConnectionDescriptor connection);

    @Override
    default NodeDescriptor merge(NodeDescriptor other) {
        if (!this.getType().equals(other.getType())) {
            return this;
        }

        mergeOptionals(this::setName, this.getName(), other.getName());
        mergeDescriptors(this::setSubscription, this.getSubscription(), other.getSubscription());
        mergeDescriptors(this::setInteraction, this.getInteraction(), other.getInteraction());
        mergeDescriptors(this::setConnection, this.getConnection(), other.getConnection());

        return this;
    }
}
