package io.naryo.infrastructure.configuration.persistence.document.node.interaction.block;

import java.util.Optional;

import io.naryo.application.configuration.source.model.node.interaction.HederaMirrorNodeBlockInteractionDescriptor;
import io.naryo.domain.node.interaction.block.InteractionMode;
import jakarta.annotation.Nullable;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@TypeAlias("hedera_mirror_node_block_interaction")
public final class HederaMirrorNodeBlockInteractionPropertiesDocument
        extends BlockInteractionPropertiesDocument
        implements HederaMirrorNodeBlockInteractionDescriptor {

    private static final Integer DEFAULT_LIMIT_PER_REQUEST = 10;
    private static final Integer DEFAULT_RETRIES_PER_REQUEST = 3;

    private @Setter @Nullable Integer limitPerRequest;
    private @Setter @Nullable Integer retriesPerRequest;

    public HederaMirrorNodeBlockInteractionPropertiesDocument(
            Integer limitPerRequest, Integer retriesPerRequest) {
        this.limitPerRequest =
                limitPerRequest != null ? limitPerRequest : DEFAULT_LIMIT_PER_REQUEST;
        this.retriesPerRequest =
                retriesPerRequest != null ? retriesPerRequest : DEFAULT_RETRIES_PER_REQUEST;
    }

    public HederaMirrorNodeBlockInteractionPropertiesDocument() {
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
