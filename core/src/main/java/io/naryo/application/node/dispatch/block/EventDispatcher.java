package io.naryo.application.node.dispatch.block;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.naryo.application.configuration.resilence.ResilienceRegistry;
import io.naryo.application.node.dispatch.Dispatcher;
import io.naryo.application.node.trigger.Trigger;
import io.naryo.application.node.trigger.disposable.DisposableTrigger;
import io.naryo.application.node.trigger.permanent.PermanentTrigger;
import io.naryo.domain.event.Event;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record EventDispatcher(CircuitBreaker circuitBreaker, Retry retry, Set<Trigger<?>> triggers)
        implements Dispatcher {

    public EventDispatcher(ResilienceRegistry resilienceRegistry, Set<Trigger<?>> triggers) {
        this(
                resilienceRegistry.getOrDefault("event-dispatcher", CircuitBreaker.class),
                resilienceRegistry.getOrDefault("event-dispatcher", Retry.class),
                new HashSet<>(triggers));
    }

    @Override
    public void dispatch(Event<?> event) {
        new HashSet<>(triggers)
                .stream()
                        .parallel()
                        .forEach(
                                trigger -> {
                                    try {
                                        consumer(event).accept(trigger);
                                    } catch (Exception e) {
                                        log.warn(
                                                "Trigger {} failed, will be retried if policy allows. Reason: {}",
                                                trigger.getClass().getSimpleName(),
                                                e.getMessage());
                                    }
                                });
    }

    @Override
    public void addTrigger(Trigger<?> trigger) {
        triggers.add(trigger);
    }

    @Override
    public void removeTrigger(Trigger<?> trigger) {
        triggers.remove(trigger);
    }

    private Consumer<Trigger<?>> consumer(Event<?> event) {
        return Decorators.ofConsumer(
                        (Trigger<?> trigger) -> {
                            if (trigger.supports(event)) {
                                if (trigger instanceof DisposableTrigger) {
                                    ((DisposableTrigger<?>) trigger)
                                            .onDispose(
                                                    ignored -> {
                                                        log.debug(
                                                                "Trigger {} disposed",
                                                                trigger.getClass().getSimpleName());
                                                        triggers.remove(trigger);
                                                    });
                                } else {
                                    ((PermanentTrigger<?>) trigger)
                                            .onExecute(
                                                    ignore ->
                                                            log.debug(
                                                                    "Trigger {} executed",
                                                                    trigger.getClass()
                                                                            .getSimpleName()));
                                }

                                Trigger<Event<?>> typedTrigger = (Trigger<Event<?>>) trigger;
                                typedTrigger.trigger(event);
                            }
                        })
                .withCircuitBreaker(circuitBreaker)
                .withRetry(retry)
                .decorate();
    }
}
