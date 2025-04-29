package io.librevents.application.event.decoder;

import java.util.Set;

import io.librevents.domain.event.contract.ContractEventParameter;
import io.librevents.domain.filter.event.EventFilterSpecification;

public interface ContractEventParameterDecoder {

    Set<ContractEventParameter<?>> decode(EventFilterSpecification specification, String logData);
}
