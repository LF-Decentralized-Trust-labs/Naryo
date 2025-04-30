package io.librevents.application.broadcaster;

import io.librevents.domain.broadcaster.Broadcaster;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BroadcasterWrapperTest {

    @Mock private Broadcaster broadcaster;
    @Mock private BroadcasterProducer producer;

    @Test
    void testBroadcasterWrapper() {
        BroadcasterWrapper wrapper = new BroadcasterWrapper(broadcaster, producer);

        assertEquals(broadcaster, wrapper.broadcaster());
        assertEquals(producer, wrapper.producer());
    }

    @Test
    void testBroadcasterWrapperNullValues() {
        assertThrows(NullPointerException.class, () -> new BroadcasterWrapper(null, producer));
        assertThrows(NullPointerException.class, () -> new BroadcasterWrapper(broadcaster, null));
    }
}
