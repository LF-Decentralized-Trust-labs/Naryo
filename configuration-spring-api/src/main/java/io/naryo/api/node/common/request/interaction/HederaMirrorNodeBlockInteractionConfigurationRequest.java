package io.naryo.api.node.common.request.interaction;

import io.naryo.domain.node.interaction.InteractionConfiguration;
import io.naryo.domain.node.interaction.block.InteractionMode;
import io.naryo.domain.node.interaction.block.hedera.HederaMirrorNodeBlockInteractionConfiguration;
import io.naryo.domain.node.interaction.block.hedera.LimitPerRequest;
import io.naryo.domain.node.interaction.block.hedera.RetriesPerRequest;
import lombok.Getter;

@Getter
public final class HederaMirrorNodeBlockInteractionConfigurationRequest
        extends InteractionConfigurationRequest {

    private final Integer limitPerRequest;
    private final Integer retriesPerRequest;

    public HederaMirrorNodeBlockInteractionConfigurationRequest(
            Integer limitPerRequest, Integer retriesPerRequest) {
        super(InteractionMode.HEDERA_MIRROR_NODE);
        this.limitPerRequest = limitPerRequest;
        this.retriesPerRequest = retriesPerRequest;
    }

    @Override
    public InteractionConfiguration toDomain() {
        return new HederaMirrorNodeBlockInteractionConfiguration(
                new LimitPerRequest(this.limitPerRequest),
                new RetriesPerRequest(this.retriesPerRequest));
    }
}
