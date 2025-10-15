package io.naryo.api.node.common.request.subscription;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import jakarta.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "strategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BlockSubscriptionConfigurationRequest.class, name = "BLOCK_BASED"),
})
public abstract class SubscriptionConfigurationRequest {

    private final @NotNull SubscriptionStrategy strategy;

    protected SubscriptionConfigurationRequest(SubscriptionStrategy strategy) {
        this.strategy = strategy;
    }

    public SubscriptionConfiguration toDomain() {
        var blockSubscription = (BlockSubscriptionConfigurationRequest) this;
        return new BlockSubscriptionConfiguration(
                blockSubscription.getMethod().toDomain(),
                blockSubscription.getInitialBlock(),
                new NonNegativeBlockNumber(blockSubscription.getConfirmationBlocks()),
                new NonNegativeBlockNumber(blockSubscription.getMissingTxRetryBlocks()),
                new NonNegativeBlockNumber(blockSubscription.getEventInvalidationBlockThreshold()),
                new NonNegativeBlockNumber(blockSubscription.getReplayBlockOffset()),
                new NonNegativeBlockNumber(blockSubscription.getSyncBlockLimit()));
    }
}
