package io.naryo.application.filter.block;

import java.util.List;
import java.util.Objects;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.naryo.application.configuration.resilence.ResilienceRegistry;
import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.filter.Synchronizer;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.FilterType;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.filter.event.sync.ActiveSyncState;
import io.naryo.domain.filter.event.sync.SyncStrategy;
import io.naryo.domain.node.Node;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class NodeSynchronizer implements Synchronizer {

    private final Node node;
    private final StartBlockCalculator calculator;
    private final BlockInteractor blockInteractor;
    private final ContractEventParameterDecoder decoder;
    private final ContractEventDispatcherHelper helper;
    private final List<Filter> filters;
    private final ResilienceRegistry resilienceRegistry;

    public NodeSynchronizer(
            Node node,
            StartBlockCalculator calculator,
            BlockInteractor blockInteractor,
            List<Filter> filters,
            ContractEventParameterDecoder decoder,
            ContractEventDispatcherHelper helper,
            ResilienceRegistry resilienceRegistry) {
        Objects.requireNonNull(node, "node must not be null");
        Objects.requireNonNull(calculator, "calculator must not be null");
        Objects.requireNonNull(blockInteractor, "blockInteractor must not be null");
        Objects.requireNonNull(filters, "filters must not be null");
        Objects.requireNonNull(decoder, "decoder must not be null");
        Objects.requireNonNull(helper, "helper must not be null");
        Objects.requireNonNull(resilienceRegistry, "resilienceRegistry must not be null");
        this.node = node;
        this.calculator = calculator;
        this.blockInteractor = blockInteractor;
        this.filters = filters;
        this.decoder = decoder;
        this.helper = helper;
        this.resilienceRegistry = resilienceRegistry;
    }

    @Override
    public Disposable synchronize() {
        List<Filter> filters =
                this.filters.stream()
                        .filter(
                                filter ->
                                        filter.getType().equals(FilterType.EVENT)
                                                && filter instanceof EventFilter ef
                                                && ef.getSyncState() instanceof ActiveSyncState ase
                                                && !ase.isSync()
                                                && ase.getStrategy()
                                                        .equals(SyncStrategy.BLOCK_BASED))
                        .toList();
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        filters.parallelStream()
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
                                                "event-filter-synchronizer", Retry.class)))
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
}
