package io.naryo.application.configuration.source.model.node;

import java.util.UUID;

import io.naryo.application.configuration.source.model.MergeableDescriptor;
import io.naryo.application.configuration.source.model.node.connection.NodeConnectionDescriptor;
import io.naryo.application.configuration.source.model.node.interaction.InteractionDescriptor;
import io.naryo.application.configuration.source.model.node.subscription.SubscriptionDescriptor;
import io.naryo.domain.node.NodeType;

public interface NodeDescriptor extends MergeableDescriptor<NodeDescriptor> {

    UUID getId();

    String getName();

    NodeType getType();

    SubscriptionDescriptor getSubscription();

    InteractionDescriptor getInteraction();

    NodeConnectionDescriptor getConnection();

    void setName(String name);

    void setSubscription(SubscriptionDescriptor subscription);

    void setInteraction(InteractionDescriptor interaction);

    void setConnection(NodeConnectionDescriptor connection);

    @Override
    default NodeDescriptor merge(NodeDescriptor descriptor) {
        if (descriptor == null) {
            return this;
        }

        if (!this.getName().equals(descriptor.getName())) {
            this.setName(descriptor.getName());
        }

        if (!this.getType().equals(descriptor.getType())) {
            return descriptor;
        }

        if (descriptor.getSubscription() != null) {
            if (this.getSubscription() == null) {
                this.setSubscription(descriptor.getSubscription());
            } else if (!descriptor
                    .getSubscription()
                    .getClass()
                    .equals(this.getSubscription().getClass())) {
                this.setSubscription(descriptor.getSubscription());
            } else {
                this.setSubscription(this.getSubscription().merge(descriptor.getSubscription()));
            }
        }

        if (descriptor.getInteraction() != null) {
            if (this.getInteraction() == null) {
                this.setInteraction(descriptor.getInteraction());
            } else {
                this.getInteraction().merge(descriptor.getInteraction());
            }
        }

        if (descriptor.getConnection() != null) {
            if (this.getConnection() == null) {
                this.setConnection(descriptor.getConnection());
            } else {
                this.getConnection().merge(descriptor.getConnection());
            }
        }

        return this;
    }
}
