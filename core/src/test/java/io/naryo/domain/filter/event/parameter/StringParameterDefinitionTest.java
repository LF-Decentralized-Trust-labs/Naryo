package io.naryo.domain.filter.event.parameter;

import io.naryo.domain.common.ParameterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringParameterDefinitionTest {

    @Test
    void testStringParameterDefinition() {
        StringParameterDefinition stringParameterDefinition =
                new StringParameterDefinition(1, false);
        assertEquals(ParameterType.STRING, stringParameterDefinition.getType());
        assertEquals(1, stringParameterDefinition.getPosition());
    }
}
