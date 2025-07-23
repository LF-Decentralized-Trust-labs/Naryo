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

    Optional<NodeType> getType();

    <T extends SubscriptionDescriptor> Optional<T> getSubscription();

    <T extends InteractionDescriptor> Optional<T> getInteraction();

    <T extends NodeConnectionDescriptor> Optional<T> getConnection();

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
        mergeDescriptors(
                this::setSubscription, this.getSubscription(), descriptor.getSubscription());
        mergeDescriptors(this::setInteraction, this.getInteraction(), descriptor.getInteraction());
        mergeDescriptors(this::setConnection, this.getConnection(), descriptor.getConnection());

        return this;
    }
}
