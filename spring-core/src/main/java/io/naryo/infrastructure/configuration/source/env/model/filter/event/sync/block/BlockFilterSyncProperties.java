package io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.block;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.application.configuration.source.model.filter.event.sync.BlockFilterSyncDescriptor;
import io.naryo.domain.filter.event.sync.SyncStrategy;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.FilterSyncProperties;
import jakarta.annotation.Nullable;
import lombok.Setter;

@Setter
public final class BlockFilterSyncProperties extends FilterSyncProperties
        implements BlockFilterSyncDescriptor {

    private @Nullable BigInteger initialBlock;

    public BlockFilterSyncProperties(BigInteger initialBlock) {
        super(SyncStrategy.BLOCK_BASED);
        this.initialBlock = initialBlock;
    }

    @Override
    public Optional<BigInteger> getInitialBlock() {
        return Optional.ofNullable(initialBlock);
    }
}
