package io.naryo.application.configuration.revision.store;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import io.naryo.application.configuration.revision.RevisionOperationState;
import io.naryo.application.configuration.revision.RevisionOperationStatus;

public class InMemoryRevisionOperationStore implements RevisionOperationStore {

    private static final class Entry {
        final String operationId;
        volatile RevisionOperationState state;
        volatile Long revision;
        volatile String hash;
        volatile String errorCode;
        volatile String errorMessage;
        volatile Instant lastUpdatedAt;
        volatile Instant acceptedAt;

        Entry(String operationId) {
            this.operationId = operationId;
            this.acceptedAt = Instant.now();
            this.lastUpdatedAt = this.acceptedAt;
            this.state = RevisionOperationState.PENDING;
        }

        void touch() {
            this.lastUpdatedAt = Instant.now();
        }

        RevisionOperationStatus snapshot() {
            return new RevisionOperationStatus(
                    operationId,
                    state,
                    revision,
                    hash,
                    errorCode,
                    errorMessage,
                    acceptedAt,
                    lastUpdatedAt);
        }
    }

    private final Map<String, Entry> entries = new ConcurrentHashMap<>();

    @Override
    public void accepted(String operationId) {
        Objects.requireNonNull(operationId, "operationId");
        entries.compute(
                operationId,
                (id, e) -> {
                    if (e == null) {
                        return new Entry(id);
                    }
                    e.touch();
                    return e;
                });
    }

    @Override
    public void running(String operationId) {
        Objects.requireNonNull(operationId, "operationId");
        entries.computeIfPresent(
                operationId,
                (id, e) -> {
                    if (e.state == RevisionOperationState.PENDING
                            || e.state == RevisionOperationState.RUNNING) {
                        e.state = RevisionOperationState.RUNNING;
                        e.touch();
                    }
                    return e;
                });
    }

    @Override
    public void succeeded(String operationId, long revision, String hash) {
        Objects.requireNonNull(operationId, "operationId");
        Objects.requireNonNull(hash, "hash");
        entries.computeIfPresent(
                operationId,
                (id, e) -> {
                    e.state = RevisionOperationState.SUCCEEDED;
                    e.revision = revision;
                    e.hash = hash;
                    e.errorCode = null;
                    e.errorMessage = null;
                    e.touch();
                    return e;
                });
    }

    @Override
    public void failed(String operationId, String errorCode, String errorMessage) {
        Objects.requireNonNull(operationId, "operationId");
        entries.computeIfPresent(
                operationId,
                (id, e) -> {
                    e.state = RevisionOperationState.FAILED;
                    e.revision = null;
                    e.hash = null;
                    e.errorCode = errorCode;
                    e.errorMessage = errorMessage;
                    e.touch();
                    return e;
                });
    }

    @Override
    public Optional<RevisionOperationStatus> get(String operationId) {
        Objects.requireNonNull(operationId, "operationId");
        var e = entries.get(operationId);
        return Optional.ofNullable(e).map(Entry::snapshot);
    }
}
