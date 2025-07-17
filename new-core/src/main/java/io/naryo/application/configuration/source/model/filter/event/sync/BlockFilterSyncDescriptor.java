package io.naryo.application.configuration.source.model.filter.event.sync;

import io.naryo.domain.filter.event.sync.SyncStrategy;

import java.math.BigInteger;
import java.util.Optional;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface BlockFilterSyncDescriptor extends FilterSyncDescriptor {

    Optional<BigInteger> getInitialBlock();

    void setInitialBlock(Optional<BigInteger> initialBlock);

    @Override
    default SyncStrategy getStrategy() {
        return SyncStrategy.BLOCK_BASED;
    }

    @Override
    default FilterSyncDescriptor merge(FilterSyncDescriptor descriptor) {
        var sync = FilterSyncDescriptor.super.merge(descriptor);

        if (sync instanceof BlockFilterSyncDescriptor other) {
            mergeOptionals(this::setInitialBlock, this.getInitialBlock(), other.getInitialBlock());
        }

        return sync;
    }
}
