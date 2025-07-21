package io.naryo.application.configuration.source.model.node.connection.retry;

import java.time.Duration;
import java.util.Optional;

import io.naryo.application.configuration.source.model.MergeableDescriptor;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface NodeConnectionRetryDescriptor
        extends MergeableDescriptor<NodeConnectionRetryDescriptor> {

    Optional<Integer> getTimes();

    Optional<Duration> getBackoff();

    void setTimes(Integer times);

    void setBackoff(Duration backoff);

    @Override
    default NodeConnectionRetryDescriptor merge(NodeConnectionRetryDescriptor other) {
        if (other == null) {
            return this;
        }

        mergeOptionals(this::setTimes, this.getTimes(), other.getTimes());
        mergeOptionals(this::setBackoff, this.getBackoff(), other.getBackoff());

        return this;
    }
}
