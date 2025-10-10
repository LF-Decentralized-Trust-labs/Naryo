package io.naryo.application.configuration.revision.store;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import io.naryo.application.configuration.revision.RevisionOperationState;
import io.naryo.application.configuration.revision.RevisionOperationStatus;

public class InMemoryRevisionOperationStore implements RevisionOperationStore {

    private static final class Entry {
        final UUID operationId;
        volatile RevisionOperationState state;
        volatile Long revision;
        volatile String hash;
        volatile String errorCode;
        volatile String errorMessage;
        volatile Instant lastUpdatedAt;
        volatile Instant acceptedAt;

        Entry(UUID operationId) {
            this.operationId = operationId;
            this.acceptedAt = Instant.now();
            this.lastUpdatedAt = this.acceptedAt;
            this.state = RevisionOperationState.PENDING;
        }

        void touch() {
            Instant now = Instant.now();
            if (!now.isAfter(this.lastUpdatedAt)) {
                now = this.lastUpdatedAt.plusNanos(1);
            }
            this.lastUpdatedAt = now;
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

    private final Map<UUID, Entry> entries = new ConcurrentHashMap<>();

    @Override
    public void accepted(UUID operationId) {
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
    public void running(UUID operationId) {
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
    public void succeeded(UUID operationId, long revision, String hash) {
        Objects.requireNonNull(operationId, "operationId");
        Objects.requireNonNull(hash, "hash");
        entries.compute(
                operationId,
                (id, e) -> {
                    if (e == null) {
                        e = new Entry(id);
                    }
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
    public void failed(UUID operationId, String errorCode, String errorMessage) {
        Objects.requireNonNull(operationId, "operationId");
        entries.compute(
                operationId,
                (id, e) -> {
                    if (e == null) {
                        e = new Entry(id);
                    }
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
    public Optional<RevisionOperationStatus> get(UUID operationId) {
        Objects.requireNonNull(operationId, "operationId");
        var e = entries.get(operationId);
        return Optional.ofNullable(e).map(Entry::snapshot);
    }
}
