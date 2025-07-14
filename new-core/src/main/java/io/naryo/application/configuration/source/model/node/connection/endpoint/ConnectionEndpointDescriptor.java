package io.naryo.application.configuration.source.model.node.connection.endpoint;

import io.naryo.application.configuration.source.model.MergeableDescriptor;

public interface ConnectionEndpointDescriptor
        extends MergeableDescriptor<ConnectionEndpointDescriptor> {

    String getUrl();

    void setUrl(String url);

    @Override
    default ConnectionEndpointDescriptor merge(ConnectionEndpointDescriptor other) {
        if (other == null) {
            return this;
        }

        if (!this.getUrl().equals(other.getUrl())) {
            this.setUrl(other.getUrl());
        }

        return this;
    }
}
