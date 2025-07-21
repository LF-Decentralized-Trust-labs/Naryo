package io.naryo.application.configuration.source.model.node.connection.endpoint;

import io.naryo.application.configuration.source.model.MergeableDescriptor;

import java.util.Optional;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface ConnectionEndpointDescriptor
        extends MergeableDescriptor<ConnectionEndpointDescriptor> {

    Optional<String> getUrl();

    void setUrl(String url);

    @Override
    default ConnectionEndpointDescriptor merge(ConnectionEndpointDescriptor other) {
        if (other == null) {
            return this;
        }

        mergeOptionals(this::setUrl, this.getUrl(), other.getUrl());

        return this;
    }
}
