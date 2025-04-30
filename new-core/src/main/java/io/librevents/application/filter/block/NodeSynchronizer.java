package io.librevents.application.filter.block;

import java.util.List;
import java.util.Objects;

import io.librevents.application.event.decoder.ContractEventParameterDecoder;
import io.librevents.application.filter.Synchronizer;
import io.librevents.application.node.calculator.StartBlockCalculator;
import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.helper.ContractEventDispatcherHelper;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.domain.filter.Filter;
import io.librevents.domain.filter.FilterRepository;
import io.librevents.domain.filter.FilterType;
import io.librevents.domain.filter.event.EventFilter;
import io.librevents.domain.filter.event.sync.ActiveSyncState;
import io.librevents.domain.filter.event.sync.SyncStrategy;
import io.librevents.domain.node.Node;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class NodeSynchronizer implements Synchronizer {

    private final Node node;
    private final Dispatcher dispatcher;
    private final StartBlockCalculator calculator;
    private final BlockInteractor blockInteractor;
    private final FilterRepository filterRepository;
    private final ContractEventParameterDecoder decoder;
    private final ContractEventDispatcherHelper helper;

    public NodeSynchronizer(
            Node node,
            Dispatcher dispatcher,
            StartBlockCalculator calculator,
            BlockInteractor blockInteractor,
            FilterRepository filterRepository,
            ContractEventParameterDecoder decoder,
            ContractEventDispatcherHelper helper) {
        Objects.requireNonNull(node, "node must not be null");
        Objects.requireNonNull(dispatcher, "dispatcher must not be null");
        Objects.requireNonNull(calculator, "calculator must not be null");
        Objects.requireNonNull(blockInteractor, "blockInteractor must not be null");
        Objects.requireNonNull(filterRepository, "filterRepository must not be null");
        Objects.requireNonNull(decoder, "decoder must not be null");
        Objects.requireNonNull(helper, "helper must not be null");
        this.node = node;
        this.dispatcher = dispatcher;
        this.calculator = calculator;
        this.blockInteractor = blockInteractor;
        this.filterRepository = filterRepository;
        this.decoder = decoder;
        this.helper = helper;
    }

    @Override
    public Disposable synchronize() {
        List<Filter> filters =
                filterRepository.findByNodeId(node.getId()).stream()
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
                                        helper))
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
