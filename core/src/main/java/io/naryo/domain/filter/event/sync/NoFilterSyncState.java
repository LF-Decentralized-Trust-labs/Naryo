package io.naryo.domain.filter.event.sync;

import io.naryo.domain.filter.event.FilterSyncState;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class NoFilterSyncState implements FilterSyncState {
    // This class represents a state where no synchronization is needed.
    // It can be used as a default or placeholder for filters that do not require synchronization.
}
