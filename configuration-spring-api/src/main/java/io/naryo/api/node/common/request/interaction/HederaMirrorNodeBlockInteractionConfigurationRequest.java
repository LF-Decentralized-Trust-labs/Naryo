package io.naryo.api.node.common.request.interaction;

import io.naryo.application.configuration.source.model.node.interaction.factory.DefaultInteractionFactory;
import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.LimitPerRequest;
import io.naryo.domain.node.interaction.block.hedera.RetriesPerRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Schema(description = "Hedera interaction configuration request")
@Getter
public final class HederaMirrorNodeBlockInteractionConfigurationRequest
        extends BlockInteractionConfigurationRequest {

    @Positive @Schema(defaultValue = "10")
    private final Integer limitPerRequest;

    @Positive @Schema(defaultValue = "3")
    private final Integer retriesPerRequest;

    public HederaMirrorNodeBlockInteractionConfigurationRequest(
            Integer limitPerRequest, Integer retriesPerRequest) {
        this.limitPerRequest = limitPerRequest;
        this.retriesPerRequest = retriesPerRequest;
    }

    @Override
    public InteractionConfiguration toDomain() {
        int resolvedLimit = limitPerRequest != null ? limitPerRequest : DefaultInteractionFactory.DEFAULT_HEDERA_LIMIT_PER_REQUEST;
        int resolvedRetries = retriesPerRequest != null ? retriesPerRequest : DefaultInteractionFactory.DEFAULT_HEDERA_RETRIES_PER_REQUEST;
        return new HederaMirrorNodeBlockInteractionConfiguration(
                new LimitPerRequest(resolvedLimit),
                new RetriesPerRequest(resolvedRetries));
    }
}
