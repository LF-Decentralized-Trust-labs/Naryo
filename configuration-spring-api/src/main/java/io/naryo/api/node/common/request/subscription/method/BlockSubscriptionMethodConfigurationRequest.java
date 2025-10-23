package io.naryo.api.node.common.request.subscription.method;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.api.node.common.request.subscription.BlockSubscriptionConfigurationRequest;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethod;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "method")
@JsonSubTypes({
    @JsonSubTypes.Type(
            value = PollBlockSubscriptionConfigurationMethodRequest.class,
            name = "POLL"),
    @JsonSubTypes.Type(
            value = PubSubBlockSubscriptionConfigurationMethodRequest.class,
            name = "PUBSUB")
})
@Schema(
    description = "Base class for subscription",
    discriminatorProperty = "method",
    discriminatorMapping = {
        @DiscriminatorMapping(value = "POLL", schema = PollBlockSubscriptionConfigurationMethodRequest.class),
        @DiscriminatorMapping(value = "PUBSUB", schema = PubSubBlockSubscriptionConfigurationMethodRequest.class),
    }
)
@Getter
public abstract class BlockSubscriptionMethodConfigurationRequest {

    public abstract BlockSubscriptionMethodConfiguration toDomain();
}
