package io.naryo.application.configuration.revision.manager;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import io.naryo.application.configuration.manager.CollectionConfigurationManager;
import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.RevisionConflictException;
import io.naryo.application.configuration.revision.diff.DiffResult;
import io.naryo.application.configuration.revision.fingerprint.DefaultRevisionFingerprinter;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.application.configuration.revision.operation.AddOperation;
import io.naryo.application.configuration.revision.operation.RemoveOperation;
import io.naryo.application.configuration.revision.operation.UpdateOperation;
import io.naryo.application.configuration.revision.registry.LiveRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public abstract class BaseConfigurationRevisionManagerTest<
        T, C extends CollectionConfigurationManager<T>, F extends DefaultRevisionFingerprinter<T>> {

    protected ConfigurationRevisionManager<T> manager;
    protected F fingerprinter;
    protected C configurationManager;
    protected LiveRegistry<T> liveRegistry;
    protected RevisionHook<T> hookSpy;

    protected final Function<T, UUID> idFn;
    protected final Class<C> configurationManagerClass;

    protected BaseConfigurationRevisionManagerTest(
            Function<T, UUID> idFn, Class<C> configurationManagerClass) {
        this.idFn = idFn;
        this.configurationManagerClass = configurationManagerClass;
    }

    protected abstract ConfigurationRevisionManager<T> createManager(
            C configurationManager, F fingerprinter, LiveRegistry<T> liveRegistry);

    protected abstract F createFingerprinter();

    protected abstract T newItem();

    protected abstract T updatedVariantOf(T base);

    protected UUID idOf(T item) {
        return idFn.apply(item);
    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        // Mocks
        configurationManager = mock(configurationManagerClass);
        liveRegistry = mock(LiveRegistry.class);
        fingerprinter = spy(createFingerprinter());

        when(configurationManager.load()).thenReturn(List.of());

        manager = createManager(configurationManager, fingerprinter, liveRegistry);

        hookSpy = mock(RevisionHook.class);
        manager.registerHook(hookSpy);
    }

    @Test
    void initialize_publishLiveViewYRefresh() throws Exception {
        Revision<T> rev = manager.initialize();

        assertNotNull(rev);
        assertEquals(0L, rev.version());
        assertNotNull(rev.hash());

        LiveView<T> lv = manager.liveView();
        assertNotNull(lv);
        assertSame(rev, lv.revision());

        verify(configurationManager, times(1)).load();
        verify(liveRegistry, times(1)).refresh(rev);
        verifyNoMoreInteractions(hookSpy);
    }

    @Test
    @SuppressWarnings("unchecked")
    void apply_addItem_and_bumpVersion_and_triggerHooks() throws Exception {
        manager.initialize();
        T item = newItem();
        String itemHash = fingerprinter.itemHash(item);

        Revision<T> after = manager.apply(new AddOperation<>(item));

        assertNotNull(after);
        assertEquals(1L, after.version());
        assertTrue(after.domainItems().stream().anyMatch(i -> idOf(i).equals(idOf(item))));
        assertEquals(itemHash, manager.liveView().itemFingerprintById().get(idFn.apply(item)));

        ArgumentCaptor<DiffResult<T>> afterDiff = ArgumentCaptor.forClass(DiffResult.class);
        verify(hookSpy, times(1)).onBeforeApply(afterDiff.capture());

        ArgumentCaptor<Revision<T>> afterApplied = ArgumentCaptor.forClass(Revision.class);

        verify(hookSpy, times(1)).onAfterApply(afterApplied.capture(), afterDiff.capture());
        assertEquals(after, afterApplied.getValue());
        assertNotNull(afterDiff.getValue());

        Revision<T> again = manager.apply(new AddOperation<>(item));
        assertSame(after, again);
        assertEquals(1L, manager.liveView().revision().version());

        verify(liveRegistry, times(2)).refresh(any());
    }

    @Test
    void apply_updateWithPrevHash_andModifyItem() throws Exception {
        manager.initialize();
        T base = newItem();
        manager.apply(new AddOperation<>(base));

        String prevHash = fingerprinter.itemHash(base);
        T updated = updatedVariantOf(base);

        Revision<T> after =
                manager.apply(new UpdateOperation<>(idFn.apply(base), prevHash, updated));

        assertEquals(2L, after.version());
        assertTrue(after.domainItems().stream().anyMatch(i -> idOf(i).equals(idOf(updated))));
        assertEquals(
                fingerprinter.itemHash(updated),
                manager.liveView().itemFingerprintById().get(idFn.apply(updated)));

        verify(hookSpy, atLeast(2)).onBeforeApply(any());
        verify(hookSpy, atLeast(2)).onAfterApply(any(), any());
        verify(liveRegistry, times(3)).refresh(any());
    }

    @Test
    void apply_updateWithIncorrectPrevHash_andThrowRevisionConflict() throws Exception {
        manager.initialize();
        T base = newItem();
        manager.apply(new AddOperation<>(base));

        String wrongPrev = "0x" + "00".repeat(32);
        T updated = updatedVariantOf(base);

        assertThrows(
                RevisionConflictException.class,
                () -> manager.apply(new UpdateOperation<>(idFn.apply(base), wrongPrev, updated)));

        verify(liveRegistry, times(2)).refresh(any());
        verify(hookSpy, times(1)).onBeforeApply(any());
        verify(hookSpy, times(1)).onAfterApply(any(), any());
    }

    @Test
    void apply_removeWithPrevHash_andRemoveItem() throws Exception {
        manager.initialize();
        T base = newItem();
        manager.apply(new AddOperation<>(base));
        String prevHash = fingerprinter.itemHash(base);

        Revision<T> after = manager.apply(new RemoveOperation<>(idFn.apply(base), prevHash));

        assertEquals(2L, after.version());
        assertTrue(after.domainItems().stream().noneMatch(i -> idOf(i).equals(idOf(base))));
        verify(liveRegistry, times(3)).refresh(any());
        verify(hookSpy, atLeast(2)).onBeforeApply(any());
        verify(hookSpy, atLeast(2)).onAfterApply(any(), any());
    }

    @Test
    void apply_removeWithIncorrectPrevHash_triggerRevisionConflict() throws Exception {
        manager.initialize();
        T base = newItem();
        manager.apply(new AddOperation<>(base));
        String wrongPrev = "0x" + "ff".repeat(32);

        assertThrows(
                RevisionConflictException.class,
                () -> manager.apply(new RemoveOperation<>(idFn.apply(base), wrongPrev)));

        verify(liveRegistry, times(2)).refresh(any());
        verify(hookSpy, times(1)).onBeforeApply(any());
        verify(hookSpy, times(1)).onAfterApply(any(), any());
    }

    @Test
    protected void apply_addWithSameIdButDifferentContent_throwRevisionConflict() throws Exception {
        manager.initialize();
        T base = newItem();
        manager.apply(new AddOperation<>(base));

        T other = updatedVariantOf(base);

        assertThrows(
                RevisionConflictException.class, () -> manager.apply(new AddOperation<>(other)));

        verify(liveRegistry, times(2)).refresh(any());

        verify(hookSpy, times(1)).onBeforeApply(any());
        verify(hookSpy, times(1)).onAfterApply(any(), any());
    }
}
