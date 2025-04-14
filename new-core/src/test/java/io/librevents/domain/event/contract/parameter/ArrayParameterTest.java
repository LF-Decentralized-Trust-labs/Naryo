package io.librevents.domain.event.contract.parameter;

import java.util.List;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.contract.AbstractContractEventParameterTest;
import io.librevents.domain.event.contract.ContractEventParameter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayParameterTest extends AbstractContractEventParameterTest {

    @Override
    @SuppressWarnings("unchecked")
    protected void createParameter(boolean indexed, int position, Object value) {
        new ArrayParameter<>(indexed, position, (List<? extends ContractEventParameter<?>>) value);
    }

    @Test
    void testConstructor() {
        AddressParameter address1 = new AddressParameter(true, 1, "0x1234567890abcdef");
        AddressParameter address2 = new AddressParameter(false, 2, "0xabcdef1234567890");
        List<AddressParameter> addresses = List.of(address1, address2);

        ArrayParameter<AddressParameter> parameter = new ArrayParameter<>(true, 1, addresses);

        assertEquals(addresses, parameter.getValue());
        assertTrue(parameter.isIndexed());
        assertEquals(1, parameter.getPosition());
        assertEquals(ParameterType.ARRAY, parameter.getType());
    }

    @Test
    void testConstructorWithEmptyValues() {
        List<AddressParameter> addresses = List.of();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ArrayParameter<>(true, 1, addresses);
        });

        String expectedMessage = "ArrayParameter value cannot be empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
