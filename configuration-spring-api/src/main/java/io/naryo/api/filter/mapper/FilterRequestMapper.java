package io.naryo.api.filter.mapper;

import io.naryo.api.filter.request.ContractEventFilterRequest;
import io.naryo.api.filter.request.FilterRequest;
import io.naryo.api.filter.request.GlobalEventFilterRequest;
import io.naryo.api.filter.request.TransactionFilterRequest;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterName;
import io.naryo.domain.filter.event.ContractEventFilter;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.event.GlobalEventFilter;
import io.naryo.domain.filter.transaction.TransactionFilter;

public final class FilterRequestMapper {
    private FilterRequestMapper() {}

    public static EventFilter toDomain(GlobalEventFilterRequest req) {
        var name = new FilterName(req.name());
        return new GlobalEventFilter(
                req.id(),
                name,
                req.nodeId(),
                req.specification(),
                req.statuses(),
                req.filterSyncState(),
                req.visibilityConfiguration());
    }

    public static EventFilter toDomain(ContractEventFilterRequest req) {
        var name = new FilterName(req.name());
        return new ContractEventFilter(
                req.id(),
                name,
                req.nodeId(),
                req.specification(),
                req.statuses(),
                req.filterSyncState(),
                req.visibilityConfiguration(),
                req.contractAddress());
    }

    public static Filter toDomain(FilterRequest req) {
        return switch (req) {
            case TransactionFilterRequest t -> toDomain(t);
            case GlobalEventFilterRequest g -> toDomain(g);
            case ContractEventFilterRequest c -> toDomain(c);
        };
    }

    public static TransactionFilter toDomain(TransactionFilterRequest req) {
        var name = new FilterName(req.name());
        return new TransactionFilter(
                req.id(), name, req.nodeId(), req.identifierType(), req.value(), req.statuses());
    }
}
