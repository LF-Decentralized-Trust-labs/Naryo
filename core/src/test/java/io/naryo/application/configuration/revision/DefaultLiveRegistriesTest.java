package io.naryo.application.configuration.revision;

import io.naryo.application.configuration.revision.impl.DefaultLiveRegistries;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.common.http.HttpClient;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class DefaultLiveRegistriesTest {

    @Test
    void getters_and_type_lookup_return_the_provided_registries() {
        LiveRegistry<BroadcasterConfiguration> broadcasterConfigurations = mock(LiveRegistry.class);
        LiveRegistry<Broadcaster> broadcasters = mock(LiveRegistry.class);
        LiveRegistry<Filter> filters = mock(LiveRegistry.class);
        LiveRegistry<Node> nodes = mock(LiveRegistry.class);
        LiveRegistry<StoreConfiguration> storeConfigurations = mock(LiveRegistry.class);
        LiveRegistry<HttpClient> httpClient = mock(LiveRegistry.class);

        DefaultLiveRegistries registries =
                new DefaultLiveRegistries(
                        broadcasterConfigurations,
                        broadcasters,
                        filters,
                        nodes,
                        storeConfigurations,
                        httpClient);

        assertEquals(registries.broadcasterConfigurations(), broadcasterConfigurations);
        assertEquals(registries.broadcasters(), broadcasters);
        assertEquals(registries.filters(), filters);
        assertEquals(registries.nodes(), nodes);
        assertEquals(registries.storeConfigurations(), storeConfigurations);
        assertEquals(registries.httpClient(), httpClient);

        assertEquals(registries.get(BroadcasterConfiguration.class), broadcasterConfigurations);
        assertEquals(registries.get(Broadcaster.class), broadcasters);
        assertEquals(registries.get(Filter.class), filters);
        assertEquals(registries.get(Node.class), nodes);
        assertEquals(registries.get(StoreConfiguration.class), storeConfigurations);
        assertEquals(registries.get(HttpClient.class), httpClient);
    }

    @Test
    void get_throws_for_unregistered_type() {
        DefaultLiveRegistries registries =
                new DefaultLiveRegistries(
                        mock(LiveRegistry.class),
                        mock(LiveRegistry.class),
                        mock(LiveRegistry.class),
                        mock(LiveRegistry.class),
                        mock(LiveRegistry.class),
                        mock(LiveRegistry.class));

        assertThrows(IllegalArgumentException.class, () -> registries.get(String.class));
    }
}
