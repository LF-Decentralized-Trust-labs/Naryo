package io.librevents.domain.filter.event.parameter;

import io.librevents.domain.filter.event.ParameterType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringParameterDefinitionTest {

    @Test
    void testStringParameterDefinition() {
        StringParameterDefinition stringParameterDefinition = new StringParameterDefinition(1);
        assertEquals(ParameterType.STRING, stringParameterDefinition.getType());
        assertEquals(1, stringParameterDefinition.getPosition());
    }
}
