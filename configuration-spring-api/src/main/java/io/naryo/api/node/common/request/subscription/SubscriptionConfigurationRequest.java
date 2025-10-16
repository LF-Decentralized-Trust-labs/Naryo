package io.naryo.api.node.common.request.subscription;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import io.naryo.domain.node.subscription.SubscriptionStrategy;
import jakarta.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "strategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BlockSubscriptionConfigurationRequest.class, name = "BLOCK_BASED"),
})
public abstract class SubscriptionConfigurationRequest {

    protected final @NotNull SubscriptionStrategy strategy;

    protected SubscriptionConfigurationRequest(SubscriptionStrategy strategy) {
        this.strategy = strategy;
    }

    public abstract SubscriptionConfiguration toDomain();
}
