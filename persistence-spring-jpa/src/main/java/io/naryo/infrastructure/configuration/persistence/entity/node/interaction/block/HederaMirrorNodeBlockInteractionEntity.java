package io.naryo.infrastructure.configuration.persistence.entity.node.interaction.block;

import java.util.Optional;

import io.naryo.application.configuration.source.model.node.interaction.HederaMirrorNodeBlockInteractionDescriptor;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Setter;

@Entity
@DiscriminatorValue("hedera")
public class HederaMirrorNodeBlockInteractionEntity extends BlockInteractionEntity
        implements HederaMirrorNodeBlockInteractionDescriptor {

    private static final Integer DEFAULT_LIMIT_PER_REQUEST = 10;
    private static final Integer DEFAULT_RETRIES_PER_REQUEST = 3;

    private @Setter @Nullable @Column(name = "limit_per_request") Integer limitPerRequest;
    private @Setter @Nullable @Column(name = "retries_per_request") Integer retriesPerRequest;

    public HederaMirrorNodeBlockInteractionEntity(
            @Nullable Integer limitPerRequest, @Nullable Integer retriesPerRequest) {
        this.limitPerRequest =
                limitPerRequest != null ? limitPerRequest : DEFAULT_LIMIT_PER_REQUEST;
        this.retriesPerRequest =
                retriesPerRequest != null ? retriesPerRequest : DEFAULT_RETRIES_PER_REQUEST;
    }

    public HederaMirrorNodeBlockInteractionEntity() {
        this(DEFAULT_LIMIT_PER_REQUEST, DEFAULT_RETRIES_PER_REQUEST);
    }

    @Override
    public Optional<Integer> getLimitPerRequest() {
        return Optional.ofNullable(this.limitPerRequest);
    }

    @Override
    public Optional<Integer> getRetriesPerRequest() {
        return Optional.ofNullable(this.retriesPerRequest);
    }
}
