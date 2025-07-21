package io.naryo.infrastructure.configuration.source.env.model.node.interaction.block;

import io.naryo.application.configuration.source.model.node.interaction.HederaMirrorNodeBlockInteractionDescriptor;
import io.naryo.domain.node.interaction.block.InteractionMode;
import jakarta.annotation.Nullable;
import lombok.Setter;

import java.util.Optional;

public class HederaMirrorNodeBlockInteractionProperties extends BlockInteractionProperties
        implements HederaMirrorNodeBlockInteractionDescriptor {

    private static final Integer DEFAULT_LIMIT_PER_REQUEST = 10;
    private static final Integer DEFAULT_RETRIES_PER_REQUEST = 3;

    private @Setter @Nullable Integer limitPerRequest;
    private @Setter @Nullable Integer retriesPerRequest;

    public HederaMirrorNodeBlockInteractionProperties(
            Integer limitPerRequest, Integer retriesPerRequest) {
        super(InteractionMode.HEDERA_MIRROR_NODE);
        this.limitPerRequest =
                limitPerRequest != null ? limitPerRequest : DEFAULT_LIMIT_PER_REQUEST;
        this.retriesPerRequest =
                retriesPerRequest != null ? retriesPerRequest : DEFAULT_RETRIES_PER_REQUEST;
    }

    public HederaMirrorNodeBlockInteractionProperties() {
        this(DEFAULT_LIMIT_PER_REQUEST, DEFAULT_RETRIES_PER_REQUEST);
    }

    @Override
    public Optional<Integer> getLimitPerRequest() {
        return Optional.ofNullable(limitPerRequest);
    }

    @Override
    public Optional<Integer> getRetriesPerRequest() {
        return Optional.ofNullable(retriesPerRequest);
    }
}
