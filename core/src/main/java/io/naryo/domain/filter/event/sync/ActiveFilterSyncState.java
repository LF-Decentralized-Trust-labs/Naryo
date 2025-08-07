package io.naryo.domain.filter.event.sync;

import io.naryo.domain.filter.event.FilterSyncState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class ActiveFilterSyncState implements FilterSyncState {

    protected final SyncStrategy strategy;

    protected ActiveFilterSyncState(SyncStrategy strategy) {
        this.strategy = strategy;
    }
}
