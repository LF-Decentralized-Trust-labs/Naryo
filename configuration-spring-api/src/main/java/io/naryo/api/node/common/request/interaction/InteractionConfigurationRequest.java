package io.naryo.api.node.common.request.interaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.InteractionStrategy;
import io.naryo.domain.node.interaction.block.InteractionMode;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "mode")
@JsonSubTypes({
    @JsonSubTypes.Type(
            value = HederaMirrorNodeBlockInteractionConfigurationRequest.class,
            name = "BLOCK_HEDERA_MIRROR_NODE"),
    @JsonSubTypes.Type(
            value = EthereumRpcBlockInteractionConfigurationRequest.class,
            name = "BLOCK_ETHEREUM_RPC")
})
@Schema(
    description = "Base class for interaction configuration",
    discriminatorProperty = "mode",
    discriminatorMapping = {
        @DiscriminatorMapping(value = "BLOCK_HEDERA_MIRROR_NODE", schema = HederaMirrorNodeBlockInteractionConfigurationRequest.class),
        @DiscriminatorMapping(value = "BLOCK_ETHEREUM_RPC", schema = EthereumRpcBlockInteractionConfigurationRequest.class)
    }
)
@Getter
public abstract class InteractionConfigurationRequest {

    public abstract InteractionConfiguration toDomain();
}
