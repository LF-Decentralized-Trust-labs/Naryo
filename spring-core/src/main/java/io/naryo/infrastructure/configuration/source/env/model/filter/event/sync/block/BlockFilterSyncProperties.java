package io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.block;

import io.naryo.application.configuration.source.model.filter.event.sync.BlockFilterSyncDescriptor;
import io.naryo.domain.filter.event.sync.SyncStrategy;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.FilterSyncProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Optional;

@Setter
@Getter
public final class BlockFilterSyncProperties extends FilterSyncProperties
        implements BlockFilterSyncDescriptor {

    private Optional<BigInteger> initialBlock;

    public BlockFilterSyncProperties(Optional<BigInteger> initialBlock) {
        super(SyncStrategy.BLOCK_BASED);
        this.initialBlock = initialBlock;
    }

}
