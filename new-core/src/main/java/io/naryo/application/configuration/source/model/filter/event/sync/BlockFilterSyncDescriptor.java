package io.naryo.application.configuration.source.model.filter.event.sync;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.domain.filter.event.sync.SyncStrategy;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface BlockFilterSyncDescriptor extends FilterSyncDescriptor {

    Optional<BigInteger> getInitialBlock();

    void setInitialBlock(BigInteger initialBlock);

    @Override
    default SyncStrategy getStrategy() {
        return SyncStrategy.BLOCK_BASED;
    }

    @Override
    default FilterSyncDescriptor merge(FilterSyncDescriptor other) {
        if (!(other instanceof BlockFilterSyncDescriptor otherBlockFilterSync)) {
            return this;
        }

        mergeOptionals(
                this::setInitialBlock,
                this.getInitialBlock(),
                otherBlockFilterSync.getInitialBlock());

        return FilterSyncDescriptor.super.merge(other);
    }
}
