package io.naryo.api.node.getAll.model.subscription;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.api.node.getAll.model.subscription.block.BlockSubscriptionConfigurationResponse;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import io.naryo.domain.node.subscription.block.BlockSubscriptionConfiguration;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "strategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BlockSubscriptionConfigurationResponse.class, name = "BLOCK_BASED")
})
@Schema(
        description = "Base class for subscription",
        discriminatorProperty = "strategy",
        discriminatorMapping = {
            @DiscriminatorMapping(
                    value = "BLOCK_BASED",
                    schema = BlockSubscriptionConfigurationResponse.class)
        })
@Getter
public abstract class SubscriptionConfigurationResponse {

    public static SubscriptionConfigurationResponse fromDomain(
            SubscriptionConfiguration subscriptionConfiguration) {
        return switch (subscriptionConfiguration) {
            case BlockSubscriptionConfiguration block ->
                    BlockSubscriptionConfigurationResponse.fromDomain(block);
            default ->
                    throw new IllegalStateException(
                            "Unexpected value: " + subscriptionConfiguration);
        };
    }
}
