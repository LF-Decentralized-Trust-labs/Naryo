package io.naryo.application.configuration.source.model.node.connection.retry;

import java.time.Duration;

import io.naryo.application.configuration.source.model.MergeableDescriptor;

public interface NodeConnectionRetryDescriptor
        extends MergeableDescriptor<NodeConnectionRetryDescriptor> {

    Integer getTimes();

    Duration getBackoff();

    void setTimes(Integer times);

    void setBackoff(Duration backoff);

    @Override
    default NodeConnectionRetryDescriptor merge(NodeConnectionRetryDescriptor other) {
        if (other == null) {
            return this;
        }

        if (!this.getTimes().equals(other.getTimes())) {
            this.setTimes(other.getTimes());
        }

        if (!this.getBackoff().equals(other.getBackoff())) {
            this.setBackoff(other.getBackoff());
        }

        return this;
    }
}
