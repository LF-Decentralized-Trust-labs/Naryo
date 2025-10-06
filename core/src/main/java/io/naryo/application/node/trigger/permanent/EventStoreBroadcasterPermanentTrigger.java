package io.naryo.application.node.trigger.permanent;

import java.util.Collection;
import java.util.Optional;

import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.store.Store;
import io.naryo.application.store.event.EventStore;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreState;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.event.Event;
import io.naryo.domain.event.EventType;
import io.naryo.domain.event.block.BlockEvent;
import io.naryo.domain.event.contract.ContractEvent;
import io.naryo.domain.event.transaction.TransactionEvent;
import io.naryo.domain.node.Node;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EventStoreBroadcasterPermanentTrigger implements PermanentTrigger<Event<?>> {

    private final EventType targetEventType;
    private final Node node;
    private final Collection<Store<?, ?, ?>> stores;
    private final LiveView<StoreConfiguration> storeConfigurations;
    private Consumer<Event<?>> consumer;

    public EventStoreBroadcasterPermanentTrigger(
            EventType targetEventType,
            Node node,
            Collection<Store<?, ?, ?>> stores,
            LiveView<StoreConfiguration> storeConfigurations) {
        this.targetEventType = targetEventType;
        this.node = node;
        this.stores = stores;
        this.storeConfigurations = storeConfigurations;
    }

    @Override
    public void trigger(Event<?> event) {
        var storeConfiguration = getNodeStoreConfiguration();
        if (storeConfiguration.isEmpty()) {
            return;
        }

        saveEventInStore(event, storeConfiguration.get());
        sendEventToConsumers(event);
    }

    @Override
    public boolean supports(Event<?> event) {
        return event.getNodeId().equals(node.getId());
    }

    @Override
    public void onExecute(Consumer<Event<?>> consumer) {
        this.consumer = consumer;
    }

    private Optional<ActiveStoreConfiguration> getNodeStoreConfiguration() {
        StoreConfiguration storeConfiguration =
                storeConfigurations.revision().domainItems().stream()
                        .filter(conf -> conf.getNodeId().equals(node.getId()))
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new IllegalStateException(
                                                "No event store configuration found for node: "
                                                        + node.getId()));

        return (storeConfiguration.getState().equals(StoreState.ACTIVE))
                ? Optional.of((ActiveStoreConfiguration) storeConfiguration)
                : Optional.empty();
    }

    private void saveEventInStore(Event<?> event, ActiveStoreConfiguration storeConfiguration) {
        try {
            var eventStore = getEventStore(storeConfiguration);
            eventStore.save(storeConfiguration, event.getKey(), event);
        } catch (Exception e) {
            log.error(
                    "Error while saving event {} to event store: {}",
                    event.getEventType(),
                    e.getMessage(),
                    e);
        }
    }

    private EventStore<ActiveStoreConfiguration, Object, Event<?>> getEventStore(
            ActiveStoreConfiguration storeConfiguration) {
        Class<? extends Event<?>> targetEventClass =
                switch (targetEventType) {
                    case BLOCK -> BlockEvent.class;
                    case CONTRACT -> ContractEvent.class;
                    case TRANSACTION -> TransactionEvent.class;
                };

        return this.stores.stream()
                .filter(
                        store ->
                                EventStore.class.isAssignableFrom(store.getClass())
                                        && store.supports(
                                                storeConfiguration.getType(), targetEventClass))
                .findFirst()
                .map(store -> (EventStore<ActiveStoreConfiguration, Object, Event<?>>) store)
                .orElseThrow(() -> new IllegalArgumentException("No event store found"));
    }

    private void sendEventToConsumers(Event<?> event) {
        if (consumer == null) {
            return;
        }

        try {
            consumer.accept(event);
        } catch (Exception e) {
            log.error(
                    "Error while consumer execution for event {}: {}",
                    event.getEventType(),
                    e.getMessage(),
                    e);
        }
    }
}
