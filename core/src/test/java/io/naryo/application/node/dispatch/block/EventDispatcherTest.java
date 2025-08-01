package io.naryo.application.node.dispatch.block;

import java.util.Set;

import io.naryo.application.configuration.resilence.ResilienceRegistry;
import io.naryo.application.node.trigger.disposable.DisposableTrigger;
import io.naryo.application.node.trigger.permanent.PermanentTrigger;
import io.naryo.domain.event.Event;
import io.reactivex.functions.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventDispatcherTest {

    private final ResilienceRegistry resilienceRegistry = new ResilienceRegistry();

    @Mock private DisposableTrigger<Event> disposableTrigger;

    @Mock private PermanentTrigger<Event> permanentTrigger;

    @Mock private Event event;

    private EventDispatcher dispatcher;

    @Test
    @SuppressWarnings("unchecked")
    void dispatchDisposable_invokesOnDisposeAndTrigger_andRemovesTrigger() throws Exception {
        when(disposableTrigger.supports(event)).thenReturn(true);

        ArgumentCaptor<Consumer<Event>> onDisposeCaptor = ArgumentCaptor.forClass(Consumer.class);
        doNothing().when(disposableTrigger).onDispose(onDisposeCaptor.capture());
        doNothing().when(disposableTrigger).trigger(event);

        dispatcher = new EventDispatcher(resilienceRegistry, Set.of(disposableTrigger));
        dispatcher.dispatch(event);

        // Verify onDispose registration and trigger() call
        verify(disposableTrigger).onDispose(any());
        verify(disposableTrigger).trigger(event);

        // Simulate disposal callback and check removal
        Consumer<Event> disposeCallback = onDisposeCaptor.getValue();
        assertNotNull(disposeCallback);
        disposeCallback.accept(event);
        assertFalse(dispatcher.triggers().contains(disposableTrigger));
    }

    @Test
    @SuppressWarnings("unchecked")
    void dispatchPermanent_invokesOnExecuteAndTrigger_andKeepsTrigger() throws Exception {
        when(permanentTrigger.supports(event)).thenReturn(true);

        ArgumentCaptor<Consumer<Event>> onExecuteCaptor = ArgumentCaptor.forClass(Consumer.class);
        doNothing().when(permanentTrigger).onExecute(onExecuteCaptor.capture());
        doNothing().when(permanentTrigger).trigger(event);

        dispatcher = new EventDispatcher(resilienceRegistry, Set.of(permanentTrigger));
        dispatcher.dispatch(event);

        verify(permanentTrigger).onExecute(any());
        verify(permanentTrigger).trigger(event);
        assertTrue(dispatcher.triggers().contains(permanentTrigger));

        // invoke onExecute callback to ensure it doesn't remove
        Consumer<Event> execCallback = onExecuteCaptor.getValue();
        assertNotNull(execCallback);
        execCallback.accept(event);
        assertTrue(dispatcher.triggers().contains(permanentTrigger));
    }

    @Test
    void unsupportedTriggers_areNotCalled() {
        when(disposableTrigger.supports(event)).thenReturn(false);
        when(permanentTrigger.supports(event)).thenReturn(false);

        dispatcher =
                new EventDispatcher(
                        resilienceRegistry, Set.of(disposableTrigger, permanentTrigger));
        dispatcher.dispatch(event);

        verify(disposableTrigger, never()).onDispose(any());
        verify(disposableTrigger, never()).trigger(any());
        verify(permanentTrigger, never()).onExecute(any());
        verify(permanentTrigger, never()).trigger(any());
    }

    @Test
    void continuesWhenOnDisposeThrows_andStillTriggers() {
        when(disposableTrigger.supports(event)).thenReturn(true);
        doThrow(new RuntimeException("onDisposeFail")).when(disposableTrigger).onDispose(any());

        dispatcher = new EventDispatcher(resilienceRegistry, Set.of(disposableTrigger));
        assertDoesNotThrow(() -> dispatcher.dispatch(event));

        verify(disposableTrigger, atLeastOnce()).onDispose(any());
        verify(disposableTrigger, never()).trigger(event);
    }

    @Test
    void continuesWhenOnExecuteThrows_andStillTriggers() {
        when(permanentTrigger.supports(event)).thenReturn(true);
        doThrow(new RuntimeException("onExecuteFail")).when(permanentTrigger).onExecute(any());

        dispatcher = new EventDispatcher(resilienceRegistry, Set.of(permanentTrigger));
        assertDoesNotThrow(() -> dispatcher.dispatch(event));

        verify(permanentTrigger, atLeastOnce()).onExecute(any());
        verify(permanentTrigger, never()).trigger(event);
    }

    @Test
    void continuesWhenTriggerThrows_andProcessesOtherTriggers() {
        when(disposableTrigger.supports(event)).thenReturn(true);
        when(permanentTrigger.supports(event)).thenReturn(true);

        doThrow(new RuntimeException("triggerFail")).when(disposableTrigger).trigger(event);
        doNothing().when(permanentTrigger).trigger(event);

        dispatcher =
                new EventDispatcher(
                        resilienceRegistry, Set.of(disposableTrigger, permanentTrigger));
        assertDoesNotThrow(() -> dispatcher.dispatch(event));

        verify(disposableTrigger, atLeastOnce()).trigger(event);
        verify(permanentTrigger, atLeastOnce()).trigger(event);
    }

    @Test
    void addRemoveGetTriggers_workAsExpected() {
        dispatcher = new EventDispatcher(resilienceRegistry, Set.of());
        assertTrue(dispatcher.triggers().isEmpty());

        dispatcher.addTrigger(disposableTrigger);
        assertTrue(dispatcher.triggers().contains(disposableTrigger));

        dispatcher.removeTrigger(disposableTrigger);
        assertFalse(dispatcher.triggers().contains(disposableTrigger));
    }
}
