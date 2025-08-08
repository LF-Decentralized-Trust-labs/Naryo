package io.naryo.application.node.trigger.permanent;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.naryo.application.store.event.EventStore;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.configuration.store.StoreState;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.event.Event;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EventStoreBroadcasterPermanentTrigger implements PermanentTrigger<Event> {

    private final Set<EventStore<?, ?, Event>> eventStores;
    private final List<StoreConfiguration> storeConfigurations;
    private Consumer<Event> consumer;

    public <C extends StoreConfiguration> EventStoreBroadcasterPermanentTrigger(
            Set<EventStore<?, ?, Event>> eventStores,
            List<StoreConfiguration> storeConfigurations) {
        this.eventStores = eventStores;
        this.storeConfigurations = storeConfigurations;
    }

    @Override
    public void onExecute(Consumer<Event> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void trigger(Event event) {
        Optional<ActiveStoreConfiguration> storeConfiguration = findConfigurationForEvent(event);
        if (storeConfiguration.isEmpty()) {
            return;
        }
        this.eventStores.stream()
                .filter(
                        eventStore ->
                                eventStore.supports(
                                        storeConfiguration.get().getType(), event.getClass()))
                .forEach(
                        eventStore -> {
                            try {
                                @SuppressWarnings("unchecked")
                                EventStore<ActiveStoreConfiguration, Object, Event> typedStore =
                                        (EventStore<ActiveStoreConfiguration, Object, Event>)
                                                eventStore;
                                typedStore.save(storeConfiguration.get(), event.getKey(), event);
                            } catch (Exception e) {
                                log.error(
                                        "Error while saving event {} to event store: {}",
                                        event.getEventType(),
                                        e.getMessage(),
                                        e);
                            }
                        });

        if (consumer != null) {
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

    @Override
    public boolean supports(Event event) {
        return true;
    }

    private Optional<ActiveStoreConfiguration> findConfigurationForEvent(Event event) {
        return storeConfigurations.stream()
                .filter(
                        configuration ->
                                configuration.getNodeId().equals(event.getNodeId())
                                        && configuration.getState().equals(StoreState.ACTIVE))
                .map(configuration -> (ActiveStoreConfiguration) configuration)
                .findFirst();
    }
}
