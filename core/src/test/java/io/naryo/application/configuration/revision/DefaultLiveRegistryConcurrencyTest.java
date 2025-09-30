package io.naryo.application.configuration.revision;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.naryo.application.configuration.revision.registry.DefaultLiveRegistry;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DefaultLiveRegistryConcurrencyTest {

    private final ExecutorService pool =
            Executors.newCachedThreadPool(
                    r -> {
                        Thread t = new Thread(r, "default-live-registry-test");
                        t.setDaemon(true);
                        return t;
                    });

    @AfterEach
    void tearDown() {
        pool.shutdownNow();
    }

    @Test
    void writer_updates_revision_correctly() {
        DefaultLiveRegistry<Integer> registry = new DefaultLiveRegistry<>();

        Revision<Integer> rev1 = new Revision<>(1, "rev1", List.of(1, 2, 3, 4, 5));
        Revision<Integer> rev2 = new Revision<>(2, "rev2", List.of(1, 2, 3, 4, 5));

        registry.refresh(rev1);
        assertSame(rev1, registry.active(), "Registry should hold rev1");

        registry.refresh(rev2);
        assertSame(rev2, registry.active(), "Registry should hold rev2");
    }

    @Timeout(5)
    @Test
    void readers_see_latest_refreshed_revision() throws Exception {
        DefaultLiveRegistry<String> registry = new DefaultLiveRegistry<>();

        int readers = 8;
        CountDownLatch go = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(readers);
        Revision<String> newRevision = new Revision<>(1, "test-hash", List.of("A", "B"));

        List<Future<Boolean>> tasks = new ArrayList<>(readers);
        for (int i = 0; i < readers; i++) {
            tasks.add(
                    pool.submit(
                            () -> {
                                go.await();
                                long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(3);
                                while (true) {
                                    Revision<String> r = registry.active();
                                    if (newRevision.equals(r)) {
                                        done.countDown();
                                        return true;
                                    }
                                    if (System.nanoTime() > deadline) {
                                        return false;
                                    }
                                    Thread.onSpinWait();
                                }
                            }));
        }

        go.countDown();

        registry.refresh(newRevision);

        boolean completed = done.await(3, TimeUnit.SECONDS);
        assertTrue(completed);
        for (Future<Boolean> f : tasks) {
            assertTrue(f.get(0, TimeUnit.SECONDS));
        }
    }

    @Test
    @Timeout(10)
    void no_torn_reads_under_high_contention_and_publications_are_observable() throws Exception {
        DefaultLiveRegistry<Integer> registry = new DefaultLiveRegistry<>();

        Revision<Integer> revisionA = new Revision<>(10, "H-A", List.of(1, 2, 3, 4, 5));
        Revision<Integer> revisionB = new Revision<>(11, "H-B", List.of(6, 7, 8));

        AtomicBoolean stop = new AtomicBoolean(false);
        AtomicInteger observedA = new AtomicInteger();
        AtomicInteger observedB = new AtomicInteger();

        Future<?> writer =
                pool.submit(
                        () -> {
                            Revision<Integer> current = revisionA;
                            while (!stop.get()) {
                                registry.refresh(current);
                                current = (current == revisionA) ? revisionB : revisionA;
                            }
                        });

        int readers = Math.max(8, Runtime.getRuntime().availableProcessors());
        List<Future<Void>> readerTasks = new ArrayList<>(readers);
        for (int i = 0; i < readers; i++) {
            readerTasks.add(
                    pool.submit(
                            () -> {
                                long end = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
                                Revision<Integer> lastSeen = null;
                                while (System.nanoTime() < end) {
                                    Revision<Integer> r = registry.active();
                                    if (r != null) {
                                        if (r == revisionA) {
                                            observedA.incrementAndGet();
                                            assertEquals(5, r.domainItems().size());
                                            assertEquals("H-A", r.hash());
                                        } else if (r == revisionB) {
                                            observedB.incrementAndGet();
                                            assertEquals(3, r.domainItems().size());
                                            assertEquals("H-B", r.hash());
                                        } else {
                                            throw new AssertionError(
                                                    "Observed unexpected Revision instance");
                                        }
                                        lastSeen = r;
                                    }
                                    Thread.onSpinWait();
                                }
                                assertNotNull(lastSeen);
                                return null;
                            }));
        }

        for (Future<Void> f : readerTasks) {
            f.get(5, TimeUnit.SECONDS);
        }
        stop.set(true);
        writer.get(2, TimeUnit.SECONDS);

        assertTrue(observedA.get() > 0);
        assertTrue(observedB.get() > 0);
    }
}
