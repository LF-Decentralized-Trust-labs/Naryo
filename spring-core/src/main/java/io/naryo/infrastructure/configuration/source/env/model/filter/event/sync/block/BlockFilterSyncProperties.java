package io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.block;

import java.math.BigInteger;

import io.naryo.application.configuration.source.model.filter.event.sync.BlockFilterSyncDescriptor;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.FilterSyncProperties;
import io.naryo.infrastructure.configuration.source.env.model.filter.event.sync.SyncType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public final class BlockFilterSyncProperties extends FilterSyncProperties
        implements BlockFilterSyncDescriptor {

    private @Getter @Setter @NotNull BigInteger initialBlock;

    public BlockFilterSyncProperties() {
        super(SyncType.BLOCK_BASED);
    }

    public BlockFilterSyncProperties(BigInteger initialBlock) {
        this();
        this.initialBlock = initialBlock;
    }
}
