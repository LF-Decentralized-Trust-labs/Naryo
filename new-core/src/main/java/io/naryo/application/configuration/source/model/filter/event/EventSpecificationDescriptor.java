package io.naryo.application.configuration.source.model.filter.event;

import io.naryo.application.configuration.source.model.MergeableDescriptor;

public interface EventSpecificationDescriptor
        extends MergeableDescriptor<EventSpecificationDescriptor> {

    String getSignature();

    Integer getCorrelationId();

    void setSignature(String signature);

    void setCorrelationId(Integer correlationId);

    @Override
    default EventSpecificationDescriptor merge(EventSpecificationDescriptor other) {
        if (other == null) {
            return this;
        }

        if (!this.getSignature().equals(other.getSignature())) {
            this.setSignature(other.getSignature());
        }

        if (!this.getCorrelationId().equals(other.getCorrelationId())) {
            this.setCorrelationId(other.getCorrelationId());
        }

        return this;
    }
}
