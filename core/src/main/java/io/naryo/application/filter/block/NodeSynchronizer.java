package io.naryo.application.filter.block;

import java.util.List;
import java.util.Objects;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.naryo.application.configuration.resilence.ResilienceRegistry;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.filter.Synchronizer;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.store.filter.FilterStore;
import io.naryo.application.store.filter.model.FilterState;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreState;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.event.sync.ActiveFilterSyncState;
import io.naryo.domain.filter.event.sync.SyncStrategy;
import io.naryo.domain.node.Node;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class NodeSynchronizer implements Synchronizer {

    private final Node node;
    private final StartBlockCalculator calculator;
    private final BlockInteractor blockInteractor;
    private final ContractEventParameterDecoder decoder;
    private final ContractEventDispatcherHelper helper;
    private final LiveRegistry<Filter> filters;
    private final ResilienceRegistry resilienceRegistry;
    private final @Nullable FilterStore<?> filterStore;
    private final StoreConfiguration storeConfiguration;

    public NodeSynchronizer(
            Node node,
            StartBlockCalculator calculator,
            BlockInteractor blockInteractor,
            LiveRegistry<Filter> filters,
            ContractEventParameterDecoder decoder,
            ContractEventDispatcherHelper helper,
            ResilienceRegistry resilienceRegistry,
            @Nullable FilterStore<?> filterStore,
            StoreConfiguration storeConfiguration) {
        Objects.requireNonNull(node, "node must not be null");
        Objects.requireNonNull(calculator, "calculator must not be null");
        Objects.requireNonNull(blockInteractor, "blockInteractor must not be null");
        Objects.requireNonNull(filters, "filters must not be null");
        Objects.requireNonNull(decoder, "decoder must not be null");
        Objects.requireNonNull(helper, "helper must not be null");
        Objects.requireNonNull(resilienceRegistry, "resilienceRegistry must not be null");
        Objects.requireNonNull(storeConfiguration, "storeConfiguration must not be null");
        this.node = node;
        this.calculator = calculator;
        this.blockInteractor = blockInteractor;
        this.filters = filters;
        this.decoder = decoder;
        this.helper = helper;
        this.resilienceRegistry = resilienceRegistry;
        this.filterStore = filterStore;
        this.storeConfiguration = storeConfiguration;
    }

    @Override
    public Disposable synchronize() {
        FilteringResult filteringResult = getFilters();
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        filteringResult.pendingFilters.parallelStream()
                .map(
                        filter ->
                                new EventFilterSynchronizer(
                                        node,
                                        (EventFilter) filter,
                                        blockInteractor,
                                        calculator,
                                        decoder,
                                        helper,
                                        resilienceRegistry.getOrDefault(
                                                "event-filter-synchronizer", CircuitBreaker.class),
                                        resilienceRegistry.getOrDefault(
                                                "event-filter-synchronizer", Retry.class),
                                        filterStore,
                                        storeConfiguration,
                                        filteringResult.syncStates.stream()
                                                .filter(
                                                        state ->
                                                                state.filterId()
                                                                        .equals(filter.getId()))
                                                .findFirst()
                                                .orElse(null)))
                .forEach(
                        synchronizer -> {
                            try {
                                synchronizer.synchronize();
                            } catch (Exception e) {
                                log.error(
                                        "Synchronization for filter {} failed",
                                        synchronizer.getFilter(),
                                        e);
                            }
                        });
        return compositeDisposable;
    }

    private FilteringResult getFilters() {
        List<Filter> filters =
                this.filters.active().domainItems().stream()
                        .filter(
                                filter ->
                                        filter.getNodeId().equals(node.getId())
                                                && filter.getType().equals(FilterType.EVENT)
                                                && filter instanceof EventFilter ef
                                                && ef.getFilterSyncState()
                                                        instanceof ActiveFilterSyncState ase
                                                && ase.getStrategy()
                                                        .equals(SyncStrategy.BLOCK_BASED))
                        .toList();

        if (filterStore != null
                && storeConfiguration.getState().equals(StoreState.ACTIVE)
                && storeConfiguration
                        instanceof ActiveStoreConfiguration activeStoreConfiguration) {
            @SuppressWarnings("unchecked")
            FilterStore<ActiveStoreConfiguration> typedStore =
                    (FilterStore<ActiveStoreConfiguration>) filterStore;
            List<FilterState> syncStates =
                    typedStore.get(
                            activeStoreConfiguration, filters.stream().map(Filter::getId).toList());

            return filterPendingSynchronization(filters, syncStates);
        }

        return new FilteringResult(filters, List.of());
    }

    private record FilteringResult(List<Filter> pendingFilters, List<FilterState> syncStates) {}

    private FilteringResult filterPendingSynchronization(
            List<Filter> filters, List<FilterState> syncStates) {
        List<Filter> filterResult =
                filters.stream()
                        .filter(
                                filter ->
                                        syncStates.stream()
                                                .noneMatch(
                                                        state ->
                                                                state.filterId()
                                                                        .equals(filter.getId())))
                        .toList();

        return new FilteringResult(filterResult, getMatchingSyncStates(filterResult, syncStates));
    }

    private List<FilterState> getMatchingSyncStates(
            List<Filter> filters, List<FilterState> syncStates) {
        return syncStates.stream()
                .filter(
                        state ->
                                filters.stream()
                                        .anyMatch(
                                                filter -> filter.getId().equals(state.filterId())))
                .toList();
    }
}
