package io.naryo.application.configuration.source.model.filter.event;

import io.naryo.application.configuration.source.model.MergeableDescriptor;

import java.util.Optional;

import static io.naryo.application.common.util.MergeUtil.mergeOptionals;

public interface EventSpecificationDescriptor
        extends MergeableDescriptor<EventSpecificationDescriptor> {

    Optional<String> getSignature();

    Optional<Integer> getCorrelationId();

    void setSignature(String signature);

    void setCorrelationId(Integer correlationId);

    @Override
    default EventSpecificationDescriptor merge(EventSpecificationDescriptor other) {
        if (other == null) {
            return this;
        }

        mergeOptionals(this::setSignature, this.getSignature(), other.getSignature());
        mergeOptionals(this::setCorrelationId, this.getCorrelationId(), other.getCorrelationId());

        return this;
    }
}
