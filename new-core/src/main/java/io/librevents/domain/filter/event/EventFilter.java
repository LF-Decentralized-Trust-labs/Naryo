package io.librevents.domain.filter.event;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.librevents.domain.common.event.ContractEventStatus;
import io.librevents.domain.filter.Filter;
import io.librevents.domain.filter.FilterName;
import io.librevents.domain.filter.FilterType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class EventFilter extends Filter {

    private final EventFilterScope scope;
    private final EventFilterSpecification specification;
    private final List<ContractEventStatus> statuses;
    private final SyncState syncState;

    protected EventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterScope scope,
            EventFilterSpecification specification,
            List<ContractEventStatus> statuses,
            SyncState syncState) {
        super(id, name, FilterType.EVENT, nodeId);
        Objects.requireNonNull(specification, "Specification cannot be null");
        Objects.requireNonNull(statuses, "Statuses cannot be null");
        Objects.requireNonNull(syncState, "SyncState cannot be null");
        if (statuses.isEmpty()) {
            throw new IllegalArgumentException("Statuses cannot be empty");
        }
        this.scope = scope;
        this.specification = specification;
        this.statuses = statuses;
        this.syncState = syncState;
    }
}
