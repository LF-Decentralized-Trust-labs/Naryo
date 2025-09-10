package io.naryo.infrastructure.event.http.filter;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.store.filter.FilterStore;
import io.naryo.application.store.filter.model.FilterState;
import io.naryo.domain.common.Destination;
import io.naryo.domain.configuration.store.active.feature.StoreFeatureType;
import io.naryo.domain.configuration.store.active.feature.filter.FilterStoreConfiguration;
import io.naryo.domain.configuration.store.active.http.HttpStoreConfiguration;
import io.naryo.infrastructure.event.http.HttpStore;
import okhttp3.OkHttpClient;

public final class HttpFilterStore extends HttpStore<UUID, FilterState>
        implements FilterStore<HttpStoreConfiguration> {

    public HttpFilterStore(OkHttpClient httpClient, ObjectMapper objectMapper) {
        super(FilterState.class, httpClient, objectMapper);
    }

    @Override
    protected Destination getDestination(HttpStoreConfiguration configuration) {
        FilterStoreConfiguration filterConfiguration =
                configuration.getFeature(StoreFeatureType.FILTER_SYNC);
        return filterConfiguration.getDestination();
    }
}
