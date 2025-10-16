package io.naryo.api.node.common.request.subscription.method;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import jakarta.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "method")
@JsonSubTypes({
    @JsonSubTypes.Type(
            value = PollBlockSubscriptionConfigurationMethodRequest.class,
            name = "POLL"),
    @JsonSubTypes.Type(
            value = PubSubBlockSubscriptionConfigurationMethodRequest.class,
            name = "PUBSUB")
})
public abstract class BlockSubscriptionMethodConfigurationRequest {

    protected final @NotNull BlockSubscriptionMethod method;

    protected BlockSubscriptionMethodConfigurationRequest(BlockSubscriptionMethod method) {
        this.method = method;
    }

    public abstract BlockSubscriptionMethodConfiguration toDomain();
}
