package io.naryo.domain.filter.event.parameter;

import io.naryo.domain.common.ParameterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddressParameterDefinitionTest {

    @Test
    void testConstructor() {
        AddressParameterDefinition addressParameterDefinition =
                new AddressParameterDefinition(1, true);
        assertEquals(ParameterType.ADDRESS, addressParameterDefinition.getType());
        assertEquals(1, addressParameterDefinition.getPosition());
        assertTrue(addressParameterDefinition.isIndexed());
    }
}
