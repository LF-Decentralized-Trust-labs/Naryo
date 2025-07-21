package io.naryo.application.configuration.source.model.node.interaction;

import java.util.Optional;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface HederaMirrorNodeBlockInteractionDescriptor extends BlockInteractionDescriptor {

    Optional<Integer> getLimitPerRequest();

    Optional<Integer> getRetriesPerRequest();

    void setLimitPerRequest(Integer limit);

    void setRetriesPerRequest(Integer retries);

    @Override
    default InteractionDescriptor merge(InteractionDescriptor other) {
        BlockInteractionDescriptor.super.merge(other);

        if (other instanceof HederaMirrorNodeBlockInteractionDescriptor otherDescriptor) {
            mergeOptionals(this::setLimitPerRequest, this.getLimitPerRequest(), otherDescriptor.getLimitPerRequest());
            mergeOptionals(this::setRetriesPerRequest, this.getRetriesPerRequest(), otherDescriptor.getRetriesPerRequest());
        }

        return this;
    }
}
