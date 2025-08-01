package io.naryo.infrastructure.configuration.persistence.entity.filter.event.sync;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.configuration.source.model.filter.event.sync.BlockFilterSyncDescriptor;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("block")
@Setter
@NoArgsConstructor
public class BlockFilterSyncEntity extends FilterSyncEntity implements BlockFilterSyncDescriptor {

    @Column(name = "initial_block")
    private @Nullable BigInteger initialBlock;

    public BlockFilterSyncEntity(BigInteger initialBlock) {
        super();
        this.initialBlock = initialBlock;
    }

    @Override
    public Optional<BigInteger> getInitialBlock() {
        return Optional.ofNullable(initialBlock);
    }
}
