package io.naryo.application.configuration.source.model.filter.event.sync;

import java.math.BigInteger;

import io.naryo.domain.filter.event.sync.SyncStrategy;

public interface BlockFilterSyncDescriptor extends FilterSyncDescriptor {

    BigInteger getInitialBlock();

    void setInitialBlock(BigInteger initialBlock);

    @Override
    default SyncStrategy getStrategy() {
        return SyncStrategy.BLOCK_BASED;
    }

    @Override
    default FilterSyncDescriptor merge(FilterSyncDescriptor other) {
        var sync = FilterSyncDescriptor.super.merge(other);

        if (sync instanceof BlockFilterSyncDescriptor otherSync) {
            if (!this.getInitialBlock().equals(otherSync.getInitialBlock())) {
                this.setInitialBlock(otherSync.getInitialBlock());
            }
        }

        return sync;
    }
}
