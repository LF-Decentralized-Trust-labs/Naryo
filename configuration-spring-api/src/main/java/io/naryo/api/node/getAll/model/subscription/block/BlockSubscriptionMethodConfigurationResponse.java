package io.naryo.api.node.getAll.model.subscription.block;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.node.subscription.block.method.BlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.poll.PollBlockSubscriptionMethodConfiguration;
import io.naryo.domain.node.subscription.block.method.pubsub.PubSubBlockSubscriptionMethodConfiguration;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "method")
@JsonSubTypes({
    @JsonSubTypes.Type(
            value = PollBlockSubscriptionConfigurationMethodResponse.class,
            name = "POLL"),
    @JsonSubTypes.Type(
            value = PubSubBlockSubscriptionConfigurationMethodResponse.class,
            name = "PUBSUB")
})
@Schema(
        description = "Base block subscription method",
        discriminatorProperty = "method",
        discriminatorMapping = {
            @DiscriminatorMapping(
                    value = "POLL",
                    schema = PollBlockSubscriptionConfigurationMethodResponse.class),
            @DiscriminatorMapping(
                    value = "PUBSUB",
                    schema = PubSubBlockSubscriptionConfigurationMethodResponse.class),
        })
@Getter
public abstract class BlockSubscriptionMethodConfigurationResponse {

    public static BlockSubscriptionMethodConfigurationResponse fromDomain(
            BlockSubscriptionMethodConfiguration blockSubscriptionMethodConfiguration) {
        return switch (blockSubscriptionMethodConfiguration) {
            case PollBlockSubscriptionMethodConfiguration poll ->
                    PollBlockSubscriptionConfigurationMethodResponse.fromDomain(poll);
            case PubSubBlockSubscriptionMethodConfiguration ignored ->
                    PubSubBlockSubscriptionConfigurationMethodResponse.fromDomain();
            default ->
                    throw new IllegalStateException(
                            "Unexpected value: " + blockSubscriptionMethodConfiguration);
        };
    }
}
