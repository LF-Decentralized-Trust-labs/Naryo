package io.naryo.application.configuration.source.model.broadcaster.configuration;

import java.time.Duration;

import lombok.Getter;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BroadcasterCacheConfigurationDescriptorTest {

    @Test
    void testMerge_expirationTime() {
        BroadcasterCacheConfigurationDescriptor original =
                new DummyBroadcasterCacheConfigurationDescriptor();
        BroadcasterCacheConfigurationDescriptor other =
                new DummyBroadcasterCacheConfigurationDescriptor();

        BroadcasterCacheConfigurationDescriptor result = original.merge(other);

        assertEquals(
                original,
                result,
                "Should return the original BroadcasterCacheConfigurationDescriptor when merging");
    }

    @Getter
    public static class DummyBroadcasterCacheConfigurationDescriptor
            implements BroadcasterCacheConfigurationDescriptor {
        private final Duration expirationTime;

        public DummyBroadcasterCacheConfigurationDescriptor() {
            this.expirationTime = Instancio.create(Duration.class);
        }
    }
}
