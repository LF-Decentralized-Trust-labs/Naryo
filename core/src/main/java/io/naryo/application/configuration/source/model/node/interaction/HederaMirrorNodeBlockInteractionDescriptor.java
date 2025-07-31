package io.naryo.application.configuration.source.model.node.interaction;

import java.util.Optional;

import io.naryo.domain.node.interaction.block.InteractionMode;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface HederaMirrorNodeBlockInteractionDescriptor extends BlockInteractionDescriptor {

    Optional<Integer> getLimitPerRequest();

    Optional<Integer> getRetriesPerRequest();

    void setLimitPerRequest(Integer limit);

    void setRetriesPerRequest(Integer retries);

    @Override
    default InteractionMode getMode() {
        return InteractionMode.HEDERA_MIRROR_NODE;
    }

    @Override
    default InteractionDescriptor merge(InteractionDescriptor other) {
        if (!(other
                instanceof
                HederaMirrorNodeBlockInteractionDescriptor otherHederaMirrorNodeBlockInteraction)) {
            return this;
        }

        mergeOptionals(
                this::setLimitPerRequest,
                this.getLimitPerRequest(),
                otherHederaMirrorNodeBlockInteraction.getLimitPerRequest());
        mergeOptionals(
                this::setRetriesPerRequest,
                this.getRetriesPerRequest(),
                otherHederaMirrorNodeBlockInteraction.getRetriesPerRequest());

        return BlockInteractionDescriptor.super.merge(otherHederaMirrorNodeBlockInteraction);
    }
}
