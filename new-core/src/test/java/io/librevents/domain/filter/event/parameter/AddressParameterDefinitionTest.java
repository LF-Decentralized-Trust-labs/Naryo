package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.common.ParameterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
