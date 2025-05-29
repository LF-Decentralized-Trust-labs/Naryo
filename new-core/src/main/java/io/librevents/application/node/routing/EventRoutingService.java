package io.librevents.application.node.routing;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import io.librevents.domain.broadcaster.Broadcaster;
import io.librevents.domain.broadcaster.BroadcasterTargetType;
import io.librevents.domain.broadcaster.target.FilterEventBroadcasterTarget;
import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.Event;
import io.librevents.domain.event.contract.ContractEvent;
import io.librevents.domain.event.contract.ContractEventParameter;
import io.librevents.domain.filter.Filter;
import io.librevents.domain.filter.FilterType;
import io.librevents.domain.filter.event.EventFilter;
import io.librevents.domain.filter.event.ParameterDefinition;

public final class EventRoutingService {
    private final List<Filter> filters;

    public EventRoutingService(List<Filter> filters) {
        this.filters = filters;
    }

    public List<Filter> getAllFilters(List<Broadcaster> broadcasters) {
        List<UUID> filterIds =
                broadcasters.stream()
                        .filter(b -> b.getTarget() instanceof FilterEventBroadcasterTarget)
                        .map(b -> ((FilterEventBroadcasterTarget) b.getTarget()).getFilterId())
                        .toList();
        return filters.stream().filter(filter -> filterIds.contains(filter.getId())).toList();
    }

    public List<Broadcaster> matchingWrappers(Event event, List<Broadcaster> broadcasters) {
        return switch (event.getEventType()) {
            case BLOCK -> filterByTypes(broadcasters, BroadcasterTargetType.BLOCK);
            case TRANSACTION -> filterByTypes(broadcasters, BroadcasterTargetType.TRANSACTION);
            case CONTRACT -> {
                List<Broadcaster> result =
                        filterByTypes(broadcasters, BroadcasterTargetType.CONTRACT_EVENT);
                List<Filter> filters =
                        getAllFilters(broadcasters).stream()
                                .filter(f -> f.getType() == FilterType.EVENT)
                                .filter(f -> f instanceof EventFilter)
                                .map(f -> (EventFilter) f)
                                .filter(evtFilter -> matches(evtFilter, (ContractEvent) event))
                                .collect(Collectors.toList());
                if (!filters.isEmpty()) {
                    List<Broadcaster> filterWrappers =
                            broadcasters.stream()
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
            List<Broadcaster> broadcasters, BroadcasterTargetType type) {
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
