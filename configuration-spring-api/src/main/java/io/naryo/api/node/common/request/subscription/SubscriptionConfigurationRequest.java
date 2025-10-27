package io.naryo.api.node.common.request.subscription;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.node.subscription.SubscriptionConfiguration;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "strategy")
@JsonSubTypes({
    @JsonSubTypes.Type(value = BlockSubscriptionConfigurationRequest.class, name = "BLOCK_BASED"),
})
@Schema(
        description = "Base class for subscription request",
        discriminatorProperty = "strategy",
        discriminatorMapping = {
            @DiscriminatorMapping(
                    value = "BLOCK_BASED",
                    schema = BlockSubscriptionConfigurationRequest.class),
        })
@Getter
public abstract class SubscriptionConfigurationRequest {

    public abstract SubscriptionConfiguration toDomain();
}
