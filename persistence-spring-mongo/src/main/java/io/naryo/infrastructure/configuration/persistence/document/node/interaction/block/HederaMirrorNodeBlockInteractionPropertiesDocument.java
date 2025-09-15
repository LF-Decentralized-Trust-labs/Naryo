package io.naryo.infrastructure.configuration.persistence.document.node.interaction.block;

import java.util.Optional;

import io.naryo.application.configuration.source.model.node.interaction.HederaMirrorNodeBlockInteractionDescriptor;
import jakarta.annotation.Nullable;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;

@TypeAlias("hedera_mirror_node_block_interaction")
public final class HederaMirrorNodeBlockInteractionPropertiesDocument
        extends BlockInteractionPropertiesDocument
        implements HederaMirrorNodeBlockInteractionDescriptor {

    private @Setter @Nullable Integer limitPerRequest;
    private @Setter @Nullable Integer retriesPerRequest;

    public HederaMirrorNodeBlockInteractionPropertiesDocument(
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
