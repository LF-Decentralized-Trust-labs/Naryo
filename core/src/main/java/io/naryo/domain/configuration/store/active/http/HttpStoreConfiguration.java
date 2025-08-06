package io.naryo.domain.configuration.store.active.http;

import java.util.Map;
import java.util.UUID;

import io.naryo.domain.common.connection.endpoint.ConnectionEndpoint;
import io.naryo.domain.configuration.store.active.ActiveStoreConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureConfiguration;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class HttpStoreConfiguration extends ActiveStoreConfiguration {

    private final ConnectionEndpoint endpoint;

    public HttpStoreConfiguration(
            UUID nodeId,
            Map<StoreFeatureType, StoreFeatureConfiguration> features,
            ConnectionEndpoint endpoint) {
        super(nodeId, () -> "http", features);
        this.endpoint = endpoint;
    }
}
