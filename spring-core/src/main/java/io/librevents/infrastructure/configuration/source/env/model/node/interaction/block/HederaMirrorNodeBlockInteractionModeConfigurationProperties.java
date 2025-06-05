package io.librevents.infrastructure.configuration.source.env.model.node.interaction.block;

import jakarta.validation.constraints.NotNull;

public record HederaMirrorNodeBlockInteractionModeConfigurationProperties(
        @NotNull Integer limitPerRequest, @NotNull Integer retriesPerRequest)
        implements BlockInteractionModeConfigurationProperties {

    private static final Integer DEFAULT_LIMIT_PER_REQUEST = 10;
    private static final Integer DEFAULT_RETRIES_PER_REQUEST = 3;

    public HederaMirrorNodeBlockInteractionModeConfigurationProperties(
            Integer limitPerRequest, Integer retriesPerRequest) {
        this.limitPerRequest =
                limitPerRequest != null ? limitPerRequest : DEFAULT_LIMIT_PER_REQUEST;
        this.retriesPerRequest =
                retriesPerRequest != null ? retriesPerRequest : DEFAULT_RETRIES_PER_REQUEST;
    }

    public HederaMirrorNodeBlockInteractionModeConfigurationProperties() {
        this(DEFAULT_LIMIT_PER_REQUEST, DEFAULT_RETRIES_PER_REQUEST);
    }
}
