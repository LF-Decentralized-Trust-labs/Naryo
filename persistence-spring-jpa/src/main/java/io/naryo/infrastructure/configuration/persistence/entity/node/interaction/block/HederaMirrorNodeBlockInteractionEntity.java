package io.naryo.infrastructure.configuration.persistence.entity.node.interaction.block;

import java.util.Optional;

import io.naryo.application.configuration.source.model.node.interaction.HederaMirrorNodeBlockInteractionDescriptor;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("hedera")
@NoArgsConstructor
public class HederaMirrorNodeBlockInteractionEntity extends BlockInteractionEntity
        implements HederaMirrorNodeBlockInteractionDescriptor {

    private @Setter @Nullable @Column(name = "limit_per_request") Integer limitPerRequest;
    private @Setter @Nullable @Column(name = "retries_per_request") Integer retriesPerRequest;

    public HederaMirrorNodeBlockInteractionEntity(
            @Nullable Integer limitPerRequest, @Nullable Integer retriesPerRequest) {
        this.limitPerRequest = limitPerRequest;
        this.retriesPerRequest = retriesPerRequest;
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
