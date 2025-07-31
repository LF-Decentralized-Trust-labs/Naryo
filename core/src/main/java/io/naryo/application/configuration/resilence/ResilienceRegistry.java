package io.naryo.application.configuration.resilence;

import java.util.HashMap;
import java.util.Map;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;

public final class ResilienceRegistry {

    private final Map<String, CircuitBreaker> circuitBreakers = new HashMap<>();
    private final Map<String, Retry> retryConfigs = new HashMap<>();

    public void register(String name, CircuitBreaker circuitBreaker) {
        circuitBreakers.put(name, circuitBreaker);
    }

    public void register(String name, Retry retry) {
        retryConfigs.put(name, retry);
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(String name, Class<T> type) {
        if (CircuitBreaker.class.isAssignableFrom(type)) {
            return (T) getOrDefaultCircuitBreaker(name);
        } else if (Retry.class.isAssignableFrom(type)) {
            return (T) getOrDefaultRetry(name);
        }
        throw new IllegalArgumentException("Unsupported type " + type);
    }

    private CircuitBreaker getOrDefaultCircuitBreaker(String name) {
        return circuitBreakers.getOrDefault(name, CircuitBreaker.ofDefaults(name));
    }

    private Retry getOrDefaultRetry(String name) {
        return retryConfigs.getOrDefault(name, Retry.ofDefaults(name));
    }
}
