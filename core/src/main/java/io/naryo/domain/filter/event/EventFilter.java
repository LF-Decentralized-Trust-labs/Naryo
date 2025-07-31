package io.naryo.domain.filter.event;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import io.naryo.domain.common.event.ContractEventStatus;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.FilterType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class EventFilter extends Filter {

    private final EventFilterScope scope;
    private final EventFilterSpecification specification;
    private final Set<ContractEventStatus> statuses;
    private final SyncState syncState;
    private final EventFilterVisibilityConfiguration visibilityConfiguration;

    protected EventFilter(
            UUID id,
            FilterName name,
            UUID nodeId,
            EventFilterScope scope,
            EventFilterSpecification specification,
            Set<ContractEventStatus> statuses,
            SyncState syncState,
            EventFilterVisibilityConfiguration visibilityConfiguration) {
        super(id, name, FilterType.EVENT, nodeId);
        Objects.requireNonNull(specification, "Specification cannot be null");
        Objects.requireNonNull(statuses, "Statuses cannot be null");
        Objects.requireNonNull(syncState, "SyncState cannot be null");
        Objects.requireNonNull(visibilityConfiguration, "VisibilityConfiguration cannot be null");
        this.scope = scope;
        this.specification = specification;
        this.statuses = statuses.isEmpty() ? Set.of(ContractEventStatus.values()) : statuses;
        this.syncState = syncState;
        this.visibilityConfiguration = visibilityConfiguration;
    }
}
