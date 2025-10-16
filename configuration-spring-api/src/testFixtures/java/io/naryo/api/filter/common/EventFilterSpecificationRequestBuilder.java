package io.naryo.api.filter.common;

import io.naryo.api.filter.common.request.EventFilterSpecificationRequest;
import org.instancio.Instancio;

public class EventFilterSpecificationRequestBuilder {

    private String eventSignature;
    private Integer correlationIdPosition;

    public EventFilterSpecificationRequestBuilder withEventSignature(String eventSignature) {
        this.eventSignature = eventSignature;
        return this;
    }

    public EventFilterSpecificationRequestBuilder withCorrelationIdPosition(Integer correlationIdPosition) {
        this.correlationIdPosition = correlationIdPosition;
        return this;
    }

    public String getEventSignature() {
        return this.eventSignature != null ? this.eventSignature : "Transfer(address,address,uint256)";
    }

    public Integer getCorrelationIdPosition() {
        return this.correlationIdPosition != null ? this.correlationIdPosition : 0;
    }

    public EventFilterSpecificationRequest build() {
        return new EventFilterSpecificationRequest(
            getEventSignature(),
            getCorrelationIdPosition()
        );
    }
}
