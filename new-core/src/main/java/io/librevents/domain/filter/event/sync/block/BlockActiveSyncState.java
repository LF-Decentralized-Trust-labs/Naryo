package io.librevents.domain.filter.event.sync.block;

import java.util.Objects;

import io.librevents.domain.common.NonNegativeBlockNumber;
import io.librevents.domain.filter.event.sync.ActiveSyncState;
import io.librevents.domain.filter.event.sync.SyncStrategy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class BlockActiveSyncState extends ActiveSyncState {

    private final NonNegativeBlockNumber initialBlock;
    private final NonNegativeBlockNumber lastBlockProcessed;

    public BlockActiveSyncState(
            NonNegativeBlockNumber initialBlock, NonNegativeBlockNumber lastBlockProcessed) {
        super(SyncStrategy.BLOCK_BASED);
        Objects.requireNonNull(initialBlock, "initialBlock cannot be null");
        Objects.requireNonNull(lastBlockProcessed, "lastBlockProcessed cannot be null");
        this.initialBlock = initialBlock;
        this.lastBlockProcessed = lastBlockProcessed;
    }
}
