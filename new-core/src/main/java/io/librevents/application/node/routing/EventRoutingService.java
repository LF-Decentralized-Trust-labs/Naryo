package io.librevents.application.node.routing;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.librevents.application.broadcaster.BroadcasterWrapper;
import io.librevents.domain.broadcaster.BroadcasterTargetType;
import io.librevents.domain.broadcaster.target.FilterEventBroadcasterTarget;
import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.Event;
import io.librevents.domain.event.contract.ContractEvent;
import io.librevents.domain.event.contract.ContractEventParameter;
import io.librevents.domain.filter.Filter;
import io.librevents.domain.filter.FilterRepository;
import io.librevents.domain.filter.FilterType;
import io.librevents.domain.filter.event.EventFilter;
import io.librevents.domain.filter.event.ParameterDefinition;

public final class EventRoutingService {
    private final FilterRepository repository;
    private volatile List<Filter> cachedFilters;

    public EventRoutingService(FilterRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
    }

    public List<Filter> getAllFilters(List<BroadcasterWrapper> wrappers) {
        if (cachedFilters == null) {
            synchronized (this) {
                if (cachedFilters == null) {
                    cachedFilters =
                            repository.findAllById(
                                    wrappers.stream()
                                            .filter(
                                                    w ->
                                                            w.broadcaster().target()
                                                                    instanceof
                                                                    FilterEventBroadcasterTarget)
                                            .map(
                                                    w ->
                                                            ((FilterEventBroadcasterTarget)
                                                                            w.broadcaster()
                                                                                    .target())
                                                                    .getFilterId())
                                            .collect(Collectors.toList()));
                }
            }
        }
        return cachedFilters;
    }

    public List<BroadcasterWrapper> matchingWrappers(
            Event event, List<BroadcasterWrapper> broadcasters) {
        return switch (event.getEventType()) {
            case BLOCK -> filterByTypes(broadcasters, BroadcasterTargetType.BLOCK);
            case TRANSACTION -> filterByTypes(broadcasters, BroadcasterTargetType.TRANSACTION);
            case CONTRACT -> {
                List<BroadcasterWrapper> result =
                        filterByTypes(broadcasters, BroadcasterTargetType.CONTRACT_EVENT);
                List<Filter> filters =
                        getAllFilters(broadcasters).stream()
                                .filter(f -> f.getType() == FilterType.EVENT)
                                .filter(f -> f instanceof EventFilter)
                                .map(f -> (EventFilter) f)
                                .filter(evtFilter -> matches(evtFilter, (ContractEvent) event))
                                .collect(Collectors.toList());
                if (!filters.isEmpty()) {
                    List<BroadcasterWrapper> filterWrappers =
                            broadcasters.stream()
                                    .filter(
                                            w ->
                                                    w.broadcaster().target().getType()
                                                            == BroadcasterTargetType.FILTER)
                                    .filter(
                                            w ->
                                                    w.broadcaster().target()
                                                            instanceof FilterEventBroadcasterTarget)
                                    .filter(
                                            w ->
                                                    filters.stream()
                                                            .anyMatch(
                                                                    f ->
                                                                            f.getId()
                                                                                    .equals(
                                                                                            ((FilterEventBroadcasterTarget)
                                                                                                            w.broadcaster()
                                                                                                                    .target())
                                                                                                    .getFilterId())))
                                    .toList();
                    result.addAll(filterWrappers);
                }
                yield result;
            }
        };
    }

    private List<BroadcasterWrapper> filterByTypes(
            List<BroadcasterWrapper> wrappers, BroadcasterTargetType type) {
        return wrappers.stream()
                .filter(
                        w -> {
                            var t = w.broadcaster().target().getType();
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
