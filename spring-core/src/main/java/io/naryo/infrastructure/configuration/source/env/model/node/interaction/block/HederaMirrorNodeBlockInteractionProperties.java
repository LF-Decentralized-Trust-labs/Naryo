package io.naryo.infrastructure.configuration.source.env.model.node.interaction.block;

import java.util.Optional;

import io.naryo.application.configuration.source.model.node.interaction.HederaMirrorNodeBlockInteractionDescriptor;
import jakarta.annotation.Nullable;
import lombok.Setter;

public class HederaMirrorNodeBlockInteractionProperties extends BlockInteractionProperties
        implements HederaMirrorNodeBlockInteractionDescriptor {

    private @Setter @Nullable Integer limitPerRequest;
    private @Setter @Nullable Integer retriesPerRequest;

    public HederaMirrorNodeBlockInteractionProperties(
            Integer limitPerRequest, Integer retriesPerRequest) {
        this.limitPerRequest = limitPerRequest;
        this.retriesPerRequest = retriesPerRequest;
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
