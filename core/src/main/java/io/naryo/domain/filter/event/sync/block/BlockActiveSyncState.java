package io.naryo.domain.filter.event.sync.block;

import java.util.Objects;

import io.naryo.domain.common.NonNegativeBlockNumber;
import io.naryo.domain.filter.event.sync.ActiveSyncState;
import io.naryo.domain.filter.event.sync.SyncStrategy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class BlockActiveSyncState extends ActiveSyncState {

    private final NonNegativeBlockNumber initialBlock;

    public BlockActiveSyncState(NonNegativeBlockNumber initialBlock) {
        super(SyncStrategy.BLOCK_BASED);
        Objects.requireNonNull(initialBlock, "initialBlock cannot be null");
        this.initialBlock = initialBlock;
    }
}
