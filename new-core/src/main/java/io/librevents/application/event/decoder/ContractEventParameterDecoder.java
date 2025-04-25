package io.librevents.application.event.decoder;

import io.librevents.domain.event.contract.ContractEventParameter;
import io.librevents.domain.filter.event.EventFilterSpecification;

import java.util.List;

public interface ContractEventParameterDecoder {

    List<ContractEventParameter<?>> decode(EventFilterSpecification specification, String logData);

}
