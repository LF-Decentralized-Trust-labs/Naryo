package io.librevents.domain.filter.event.sync;

import io.librevents.domain.filter.event.SyncState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class ActiveSyncState implements SyncState {

    protected final SyncStrategy strategy;
    protected boolean isSync;

    protected ActiveSyncState(SyncStrategy strategy) {
        this.strategy = strategy;
        this.isSync = false;
    }

    public void setSync(boolean isSync) {
        this.isSync = isSync;
    }
}
