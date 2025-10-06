package io.naryo.application.node.routing;

import java.util.*;
import java.util.stream.Collectors;

import io.naryo.application.configuration.revision.LiveView;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterTargetType;
import io.naryo.domain.broadcaster.target.FilterEventBroadcasterTarget;
import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.domain.event.contract.ContractEventParameter;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.event.ParameterDefinition;

public final class EventRoutingService {
    private final LiveView<Filter> filters;

    public EventRoutingService(LiveView<Filter> filters) {
        this.filters = filters;
    }

    public List<Filter> getAllFilters(Collection<Broadcaster> broadcasters) {
        List<UUID> filterIds =
                broadcasters.stream()
                        .filter(b -> b.getTarget() instanceof FilterEventBroadcasterTarget)
                        .map(b -> ((FilterEventBroadcasterTarget) b.getTarget()).getFilterId())
                        .toList();

        return filters.revision().domainItems().stream()
                .filter(filter -> filterIds.contains(filter.getId()))
                .toList();
    }

    public List<Broadcaster> matchingWrappers(Event<?> event, LiveView<Broadcaster> broadcasters) {
        Collection<Broadcaster> broadcasterList = broadcasters.revision().domainItems();
        return switch (event.getEventType()) {
            case BLOCK -> filterByTypes(broadcasterList, BroadcasterTargetType.BLOCK);
            case TRANSACTION -> filterByTypes(broadcasterList, BroadcasterTargetType.TRANSACTION);
            case CONTRACT -> {
                List<Broadcaster> result =
                        filterByTypes(broadcasterList, BroadcasterTargetType.CONTRACT_EVENT);
                List<Filter> filters =
                        getAllFilters(broadcasterList).stream()
                                .filter(f -> f.getType() == FilterType.EVENT)
                                .filter(f -> f instanceof EventFilter)
                                .map(f -> (EventFilter) f)
                                .filter(evtFilter -> matches(evtFilter, (ContractEvent) event))
                                .collect(Collectors.toList());
                if (!filters.isEmpty()) {
                    List<Broadcaster> filterWrappers =
                            broadcasterList.stream()
                                    .filter(
                                            b ->
                                                    b.getTarget().getType()
                                                            == BroadcasterTargetType.FILTER)
                                    .filter(
                                            b ->
                                                    b.getTarget()
                                                            instanceof FilterEventBroadcasterTarget)
                                    .filter(
                                            b ->
                                                    filters.stream()
                                                            .anyMatch(
                                                                    f ->
                                                                            f.getId()
                                                                                    .equals(
                                                                                            ((FilterEventBroadcasterTarget)
                                                                                                            b
                                                                                                                    .getTarget())
                                                                                                    .getFilterId())))
                                    .toList();
                    result.addAll(filterWrappers);
                }
                yield result;
            }
        };
    }

    private List<Broadcaster> filterByTypes(
            Collection<Broadcaster> broadcasters, BroadcasterTargetType type) {
        return broadcasters.stream()
                .filter(
                        b -> {
                            var t = b.getTarget().getType();
                            return t == type || t == BroadcasterTargetType.ALL;
                        })
                .collect(Collectors.toList());
    }

    private boolean matches(EventFilter filter, ContractEvent event) {
        if (!filter.getStatuses().contains(event.getStatus())) {
            return false;
        }
        var spec = filter.getSpecification();
        if (!spec.eventName().equals(event.getName())) {
            return false;
        }
        return parametersMatch(spec.parameters(), event.getParameters());
    }

    private boolean parametersMatch(
            Set<ParameterDefinition> defs, Set<ContractEventParameter<?>> params) {

        if (defs.size() != params.size()) {
            return false;
        }

        Iterator<ParameterDefinition> defIt = defs.iterator();
        Iterator<ContractEventParameter<?>> parIt = params.iterator();

        while (defIt.hasNext()) {
            ParameterType defType = defIt.next().getType();
            ParameterType paramType = parIt.next().getType();

            if (!defType.equals(paramType)) {
                return false;
            }
        }
        return true;
    }
}
