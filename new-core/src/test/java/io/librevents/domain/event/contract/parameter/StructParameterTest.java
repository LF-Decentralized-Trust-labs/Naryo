package io.librevents.domain.event.contract.parameter;

import java.util.List;

import io.librevents.domain.common.ParameterType;
import io.librevents.domain.event.contract.AbstractContractEventParameterTest;
import io.librevents.domain.event.contract.ContractEventParameter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StructParameterTest extends AbstractContractEventParameterTest {

    @Override
    @SuppressWarnings("unchecked")
    protected void createParameter(boolean indexed, int position, Object value) {
        new StructParameter(indexed, position, (List<ContractEventParameter<?>>) value);
    }

    @Test
    void testConstructor() {
        StructParameter parameter =
                new StructParameter(
                        true,
                        1,
                        List.of(new AddressParameter(true, 1, "0x1234567890abcdef")));

        assertTrue(parameter.isIndexed());
        assertEquals(1, parameter.getPosition());
        assertEquals(ParameterType.STRUCT, parameter.getType());
        assertEquals(1, parameter.getValue().size());
        assertTrue(parameter.getValue().getFirst() instanceof AddressParameter);
    }

    @Test
    void testConstructorWithEmptyValues() {
        List<ContractEventParameter<?>> values = List.of();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new StructParameter(true, 1, values);
        });

        String expectedMessage = "StructParameter value cannot be empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
