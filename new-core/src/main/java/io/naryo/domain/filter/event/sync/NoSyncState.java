package io.naryo.domain.filter.event.sync;

import io.naryo.domain.filter.event.SyncState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class NoSyncState implements SyncState {
    // This class represents a state where no synchronization is needed.
    // It can be used as a default or placeholder for filters that do not require synchronization.
}
