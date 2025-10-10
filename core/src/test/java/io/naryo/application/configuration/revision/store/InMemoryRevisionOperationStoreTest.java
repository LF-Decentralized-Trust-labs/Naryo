package io.naryo.application.configuration.revision.store;

import io.naryo.application.configuration.revision.RevisionOperationState;
import io.naryo.application.configuration.revision.RevisionOperationStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRevisionOperationStoreTest {

    @Test
    void accepted_op_is_pending_and_timestamped() {
        InMemoryRevisionOperationStore store = new InMemoryRevisionOperationStore();
        UUID opId = UUID.randomUUID();

        store.accepted(opId);

        RevisionOperationStatus status = store.get(opId).orElseThrow();
        assertEquals(RevisionOperationState.PENDING, status.state());
        assertNotNull(status.acceptedAt());
        assertNotNull(status.lastUpdatedAt());
        assertNull(status.revision());
        assertNull(status.hash());
        assertNull(status.errorCode());
        assertNull(status.errorMessage());
    }

    @Test
    void accepted_op_is_idempotent_and_updates_lastUpdated_only() throws InterruptedException {
        InMemoryRevisionOperationStore store = new InMemoryRevisionOperationStore();
        UUID opId = UUID.randomUUID();

        store.accepted(opId);
        RevisionOperationStatus first = store.get(opId).orElseThrow();
        Thread.sleep(5);
        store.accepted(opId);
        RevisionOperationStatus second = store.get(opId).orElseThrow();

        assertEquals(first.acceptedAt(), second.acceptedAt());
        assertTrue(second.lastUpdatedAt().isAfter(first.lastUpdatedAt()));
        assertEquals(RevisionOperationState.PENDING, second.state());
    }

    @Test
    void running_op_transitions_from_Pending_to_Running() {
        InMemoryRevisionOperationStore store = new InMemoryRevisionOperationStore();
        UUID opId = UUID.randomUUID();

        store.accepted(opId);
        store.running(opId);

        RevisionOperationStatus status = store.get(opId).orElseThrow();
        assertEquals(RevisionOperationState.RUNNING, status.state());
    }

    @Test
    void running_op_is_idempotent_when_already_Running() throws InterruptedException {
        InMemoryRevisionOperationStore store = new InMemoryRevisionOperationStore();
        UUID opId = UUID.randomUUID();

        store.accepted(opId);
        store.running(opId);
        RevisionOperationStatus first = store.get(opId).orElseThrow();

        Thread.sleep(5);

        store.running(opId);
        RevisionOperationStatus second = store.get(opId).orElseThrow();

        assertEquals(RevisionOperationState.RUNNING, second.state());
        assertTrue(second.lastUpdatedAt().isAfter(first.lastUpdatedAt()));
    }

    @Test
    void succeeded_sets_succeeded_state_and_clears_errors_and_stores_revision_and_hash() {
        InMemoryRevisionOperationStore store = new InMemoryRevisionOperationStore();
        UUID opId = UUID.randomUUID();

        store.accepted(opId);
        store.running(opId);
        store.succeeded(opId, 42L, "abc123");

        RevisionOperationStatus status = store.get(opId).orElseThrow();
        assertEquals(RevisionOperationState.SUCCEEDED, status.state());
        assertEquals(42L, status.revision());
        assertEquals("abc123", status.hash());
        assertNull(status.errorCode());
        assertNull(status.errorMessage());
    }

    @Test
    void failed_sets_failed_state_andStoresError_andClearsOutput() {
        InMemoryRevisionOperationStore store = new InMemoryRevisionOperationStore();
        UUID opId = UUID.randomUUID();

        store.accepted(opId);
        store.running(opId);
        store.failed(opId, "Error_TEST", "Something went wrong");

        RevisionOperationStatus status = store.get(opId).orElseThrow();
        assertEquals(RevisionOperationState.FAILED, status.state());
        assertEquals("Error_TEST", status.errorCode());
        assertEquals("Something went wrong", status.errorMessage());
        assertNull(status.revision());
        assertNull(status.hash());
    }

    @Test
    void succeeded_overwrites_previous_Failure_idempotent_rewrite_to_final_state() {
        InMemoryRevisionOperationStore store = new InMemoryRevisionOperationStore();
        UUID opId = UUID.randomUUID();

        store.accepted(opId);
        store.failed(opId, "E_OLD", "Old failure");
        RevisionOperationStatus failed = store.get(opId).orElseThrow();
        assertEquals(RevisionOperationState.FAILED, failed.state());
        assertEquals("E_OLD", failed.errorCode());

        store.succeeded(opId, 7L, "hash7");
        RevisionOperationStatus succeeded = store.get(opId).orElseThrow();
        assertEquals(RevisionOperationState.SUCCEEDED, succeeded.state());
        assertEquals(7L, succeeded.revision());
        assertEquals("hash7", succeeded.hash());
        assertNull(succeeded.errorCode());
        assertNull(succeeded.errorMessage());

        RevisionOperationStatus before = store.get(opId).orElseThrow();
        store.succeeded(opId, 7L, "hash7");
        RevisionOperationStatus after = store.get(opId).orElseThrow();
        assertEquals(RevisionOperationState.SUCCEEDED, after.state());
        assertEquals(7L, after.revision());
        assertEquals("hash7", after.hash());
        assertTrue(after.lastUpdatedAt().isAfter(before.lastUpdatedAt()));
    }

    @Test
    void get_returns_empty_for_unknown_operation() {
        InMemoryRevisionOperationStore store = new InMemoryRevisionOperationStore();
        assertTrue(store.get(UUID.randomUUID()).isEmpty());
    }
}
