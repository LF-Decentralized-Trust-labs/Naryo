package io.librevents.domain.filter.event.sync;

import io.librevents.domain.filter.event.SyncState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class ActiveSyncState implements SyncState {

    protected final SyncStrategy strategy;
    @Setter protected boolean isSync;

    protected ActiveSyncState(SyncStrategy strategy) {
        this.strategy = strategy;
        this.isSync = false;
    }
}
