package io.naryo.chain.contract;

import io.naryo.dto.event.ContractEventDetails;
import io.naryo.integration.eventstore.EventStore;

public abstract class BaseContractEventListener implements ContractEventListener {

    protected EventStore eventStore;

    BaseContractEventListener(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    protected boolean isExistingEvent(ContractEventDetails eventDetails) {
        return eventStore
                .getContractEvent(
                        eventDetails.getEventSpecificationSignature(),
                        eventDetails.getAddress(),
                        eventDetails.getBlockHash(),
                        eventDetails.getTransactionHash(),
                        eventDetails.getLogIndex())
                .isEmpty();
    }
}
