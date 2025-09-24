package io.naryo.application.configuration.revision;

import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.common.http.HttpClient;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.store.StoreConfiguration;
import io.naryo.domain.filter.Filter;
import io.naryo.domain.node.Node;

public interface LiveRegistries {

    LiveRegistry<BroadcasterConfiguration> broadcasterConfigurations();

    LiveRegistry<Broadcaster> broadcasters();

    LiveRegistry<Filter> filters();

    LiveRegistry<Node> nodes();

    LiveRegistry<StoreConfiguration> storeConfigurations();

    LiveRegistry<HttpClient> httpClient();

    <T> LiveRegistry<T> get(Class<T> domainClass);
}
