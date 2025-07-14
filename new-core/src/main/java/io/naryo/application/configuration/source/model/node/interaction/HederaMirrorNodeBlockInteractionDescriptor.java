package io.naryo.application.configuration.source.model.node.interaction;

public interface HederaMirrorNodeBlockInteractionDescriptor extends BlockInteractionDescriptor {

    Integer getLimitPerRequest();

    Integer getRetriesPerRequest();

    void setLimitPerRequest(Integer limit);

    void setRetriesPerRequest(Integer retries);

    @Override
    default InteractionDescriptor merge(InteractionDescriptor other) {
        var interaction = BlockInteractionDescriptor.super.merge(other);

        if (interaction instanceof HederaMirrorNodeBlockInteractionDescriptor otherDescriptor) {
            if (!this.getLimitPerRequest().equals(otherDescriptor.getLimitPerRequest())) {
                this.setLimitPerRequest(otherDescriptor.getLimitPerRequest());
            }

            if (!this.getRetriesPerRequest().equals(otherDescriptor.getRetriesPerRequest())) {
                this.setRetriesPerRequest(otherDescriptor.getRetriesPerRequest());
            }
        }

        return interaction;
    }
}
