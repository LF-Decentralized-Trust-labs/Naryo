package io.librevents.domain.event.contract.parameter;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.contract.ContractEventParameter;

public final class StringParameter extends ContractEventParameter<String> {
    public StringParameter(boolean indexed, int position, String value) {
        super(ParameterType.STRING, indexed, position, value);
    }
}
