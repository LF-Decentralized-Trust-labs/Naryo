package io.naryo.api.node.getAll.model.interaction;

import io.naryo.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Hedera interaction configuration")
@Getter
public final class HederaMirrorNodeBlockInteractionConfigurationResponse
        extends BlockInteractionConfigurationResponse {

    private final Integer limitPerRequest;
    private final Integer retriesPerRequest;

    public HederaMirrorNodeBlockInteractionConfigurationResponse(
            Integer limitPerRequest, Integer retriesPerRequest) {
        this.limitPerRequest = limitPerRequest;
        this.retriesPerRequest = retriesPerRequest;
    }

    public static HederaMirrorNodeBlockInteractionConfigurationResponse fromDomain(
            HederaMirrorNodeBlockInteractionConfiguration
                    hederaMirrorNodeBlockInteractionConfiguration) {
        return new HederaMirrorNodeBlockInteractionConfigurationResponse(
                hederaMirrorNodeBlockInteractionConfiguration.getLimitPerRequest().value(),
                hederaMirrorNodeBlockInteractionConfiguration.getRetriesPerRequest().value());
    }
}
