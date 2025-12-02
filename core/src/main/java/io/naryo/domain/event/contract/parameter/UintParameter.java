package io.naryo.domain.event.contract.parameter;

import java.math.BigInteger;

import io.naryo.domain.common.ParameterType;
import io.naryo.domain.event.contract.ContractEventParameter;

public final class UintParameter extends ContractEventParameter<BigInteger> {
    public UintParameter(boolean indexed, int position, BigInteger value) {
        super(ParameterType.UINT, indexed, position, value);
        if (value.signum() == -1) {
            throw new IllegalArgumentException("Invalid uint value: " + value);
        }
    }
}
