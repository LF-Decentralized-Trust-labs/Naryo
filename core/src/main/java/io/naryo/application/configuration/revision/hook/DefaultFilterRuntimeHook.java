package io.naryo.application.configuration.revision.hook;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.naryo.application.configuration.resilence.ResilienceRegistry;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.filter.block.EventFilterSynchronizer;
import io.naryo.application.node.NodeLifecycle;
import io.naryo.application.node.NodeRunner;
import io.naryo.application.node.calculator.StartBlockCalculator;
import io.naryo.application.node.helper.ContractEventDispatcherHelper;
import io.naryo.application.node.interactor.block.BlockInteractor;
import io.naryo.application.node.subscription.Subscriber;
import io.naryo.application.store.Store;
import io.naryo.application.store.filter.FilterStore;
import io.naryo.application.store.filter.model.FilterState;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreState;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.filter.event.EventFilter;
import io.naryo.domain.node.Node;
import io.naryo.domain.node.subscription.SubscriptionStrategy;

public record DefaultFilterRuntimeHook(
        NodeLifecycle nodeLifecycle,
        ContractEventParameterDecoder decoder,
        ResilienceRegistry resilienceRegistry,
        LiveRegistry<StoreConfiguration> storeLiveRegistry,
        Set<Store<?, ?, ?>> stores)
        implements RevisionHook<Filter> {

    @Override
    public void onAfterApply(Revision<Filter> applied, DiffResult<Filter> diff) throws Exception {
        if (diff.added().isEmpty()) {
            return;
        }

        synchronizeFilters(diff.added());
    }

    private void synchronizeFilters(List<Filter> filters) throws IOException {
        for (Filter filter : filters) {
            final NodeRunner runner = nodeLifecycle.getRunner(filter.getNodeId());
            final Subscriber subscriber = runner.getSubscriber();
            final Node node = runner.getNode();

            if (!isBlockBased(node)) {
                continue;
            }

            final StoreConfiguration storeConfiguration = resolveStoreConfigurationFor(node);
            final FilterState filterState = resolveFilterState(filter, storeConfiguration);
            final BlockInteractor interactor = (BlockInteractor) subscriber.getInteractor();

            final EventFilterSynchronizer synchronizer =
                    new EventFilterSynchronizer(
                            node,
                            (EventFilter) filter,
                            interactor,
                            new StartBlockCalculator(node, interactor),
                            decoder,
                            new ContractEventDispatcherHelper(runner.getDispatcher(), interactor),
                            resilienceRegistry.getOrDefault(
                                    "event-filter-synchronizer", CircuitBreaker.class),
                            resilienceRegistry.getOrDefault(
                                    "event-filter-synchronizer", Retry.class),
                            null,
                            storeConfiguration,
                            filterState);

            runner.addWorkflow(synchronizer.synchronize());
        }
    }

    private boolean isBlockBased(Node node) {
        return Objects.equals(
                node.getSubscriptionConfiguration().getStrategy(),
                SubscriptionStrategy.BLOCK_BASED);
    }

    private StoreConfiguration resolveStoreConfigurationFor(Node node) {
        return storeLiveRegistry.active().domainItems().stream()
                .filter(store -> Objects.equals(store.getNodeId(), node.getId()))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private FilterState resolveFilterState(Filter filter, StoreConfiguration storeConfiguration) {
        if (!(storeConfiguration instanceof ActiveStoreConfiguration activeStoreConfiguration)) {
            return null;
        }

        final FilterStore<?> filterStore = storeForNode(storeConfiguration).orElse(null);
        if (filterStore == null) {
            return null;
        }

        final FilterStore<ActiveStoreConfiguration> typedStore =
                (FilterStore<ActiveStoreConfiguration>) filterStore;

        return typedStore.get(activeStoreConfiguration, filter.getId()).orElse(null);
    }

    private Optional<FilterStore<?>> storeForNode(StoreConfiguration configuration) {
        List<FilterStore<?>> eventStores =
                stores.stream()
                        .filter(store -> FilterStore.class.isAssignableFrom(store.getClass()))
                        .map(store -> (FilterStore<?>) store)
                        .collect(Collectors.toUnmodifiableList());
        if (configuration.getState().equals(StoreState.ACTIVE) && !eventStores.isEmpty()) {
            ActiveStoreConfiguration storeConfiguration =
                    ((ActiveStoreConfiguration) configuration);
            return Optional.of(
                    eventStores.stream()
                            .filter(
                                    store ->
                                            store.supports(
                                                    storeConfiguration.getType(),
                                                    FilterStore.class))
                            .findFirst()
                            .orElseThrow(
                                    () -> new IllegalArgumentException("No event store founded")));
        }
        return Optional.empty();
    }
}
