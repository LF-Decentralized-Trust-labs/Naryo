package io.librevents.application.broadcaster.configuration;

import java.util.Map;
import java.util.Set;

import io.librevents.application.broadcaster.BroadcasterProducer;
import io.librevents.domain.broadcaster.BroadcasterType;
import io.librevents.domain.broadcaster.Destination;
import io.librevents.domain.broadcaster.configuration.BroadcasterConfiguration;
import io.librevents.domain.broadcaster.target.AllBroadcasterTarget;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class DefaultBroadcasterConfigurationManagerTest {

    @Mock private BroadcasterProducer producer;

    @Mock private BroadcasterConfigurationProvider configurationProvider;

    @Mock private BroadcasterType broadcasterType;

    @Mock private BroadcasterConfiguration broadcasterConfiguration;

    @Test
    void testConstructor_withNullValues() {
        assertThrows(
                NullPointerException.class,
                () -> new DefaultBroadcasterConfigurationManager(null, Set.of(producer)));
        assertThrows(
                NullPointerException.class,
                () ->
                        new DefaultBroadcasterConfigurationManager(
                                Set.of(configurationProvider), null));
    }

    @Test
    void testRegisterConfiguration_withNullValues() {
        DefaultBroadcasterConfigurationManager manager =
                new DefaultBroadcasterConfigurationManager(
                        Set.of(configurationProvider), Set.of(producer));

        assertThrows(
                NullPointerException.class,
                () -> manager.registerConfiguration(null, "name", Map.of()));
        assertThrows(
                NullPointerException.class,
                () -> manager.registerConfiguration(broadcasterType, null, Map.of()));
        assertThrows(
                NullPointerException.class,
                () -> manager.registerConfiguration(broadcasterType, "name", null));
    }

    @Test
    void testRegisterConfiguration_providerNotFound() {
        doReturn(false).when(configurationProvider).supports(any());
        DefaultBroadcasterConfigurationManager manager =
                new DefaultBroadcasterConfigurationManager(
                        Set.of(configurationProvider), Set.of(producer));
        assertThrows(
                IllegalArgumentException.class,
                () -> manager.registerConfiguration(broadcasterType, "name", Map.of()));
    }

    @Test
    void testRegisterConfiguration() {
        doReturn(true).when(configurationProvider).supports(any());
        doReturn(broadcasterConfiguration).when(configurationProvider).create(any());

        DefaultBroadcasterConfigurationManager manager =
                new DefaultBroadcasterConfigurationManager(
                        Set.of(configurationProvider), Set.of(producer));

        assertDoesNotThrow(() -> manager.registerConfiguration(broadcasterType, "name", Map.of()));
    }

    @Test
    void testGetConfiguration() {
        DefaultBroadcasterConfigurationManager manager =
                new DefaultBroadcasterConfigurationManager(
                        Set.of(configurationProvider), Set.of(producer));

        assertDoesNotThrow(() -> manager.getConfiguration("name"));
    }

    @Test
    void testRegisterBroadcaster_withNullValues() {
        DefaultBroadcasterConfigurationManager manager =
                new DefaultBroadcasterConfigurationManager(
                        Set.of(configurationProvider), Set.of(producer));

        assertThrows(NullPointerException.class, () -> manager.registerBroadcaster(null, null));
        assertThrows(NullPointerException.class, () -> manager.registerBroadcaster("name", null));
    }

    @Test
    void testRegisterBroadcaster_configurationNotFound() {
        DefaultBroadcasterConfigurationManager manager =
                new DefaultBroadcasterConfigurationManager(
                        Set.of(configurationProvider), Set.of(producer));

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        manager.registerBroadcaster(
                                "name", new AllBroadcasterTarget(new Destination("test"))));
    }

    @Test
    void testRegisterBroadcaster_producerNotFound() {
        doReturn(true).when(configurationProvider).supports(any());
        doReturn(broadcasterConfiguration).when(configurationProvider).create(any());

        DefaultBroadcasterConfigurationManager manager =
                new DefaultBroadcasterConfigurationManager(
                        Set.of(configurationProvider), Set.of(producer));

        manager.registerConfiguration(broadcasterType, "name", Map.of());

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        manager.registerBroadcaster(
                                "name", new AllBroadcasterTarget(new Destination("test"))));
    }

    @Test
    void testRegisterBroadcaster() {
        doReturn(true).when(configurationProvider).supports(any());
        doReturn(broadcasterConfiguration).when(configurationProvider).create(any());
        doReturn(true).when(producer).supports(any());

        DefaultBroadcasterConfigurationManager manager =
                new DefaultBroadcasterConfigurationManager(
                        Set.of(configurationProvider), Set.of(producer));

        manager.registerConfiguration(broadcasterType, "name", Map.of());

        assertDoesNotThrow(
                () ->
                        manager.registerBroadcaster(
                                "name", new AllBroadcasterTarget(new Destination("test"))));
    }

    @Test
    void testGetWrappers() {
        DefaultBroadcasterConfigurationManager manager =
                new DefaultBroadcasterConfigurationManager(
                        Set.of(configurationProvider), Set.of(producer));

        assertDoesNotThrow(manager::wrappers);
    }
}
