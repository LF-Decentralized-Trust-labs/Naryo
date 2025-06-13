package io.naryo.application.event.decoder;

import java.util.Set;

import io.naryo.domain.event.contract.ContractEventParameter;
import io.naryo.domain.filter.event.EventFilterSpecification;

public interface ContractEventParameterDecoder {

    Set<ContractEventParameter<?>> decode(EventFilterSpecification specification, String logData);
}
