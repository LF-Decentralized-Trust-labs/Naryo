package io.librevents.domain.filter.event;

import java.util.Set;

import io.librevents.domain.filter.event.parameter.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventFilterSpecificationTest {

    @Test
    void testEventFilterSpecification() {
        EventName name = new EventName("Test");
        CorrelationId correlationId = new CorrelationId(0);
        ParameterDefinition parameterDefinition = new BoolParameterDefinition(0, false);

        EventFilterSpecification eventFilterSpecification =
                new EventFilterSpecification(name, correlationId, Set.of(parameterDefinition));

        assertEquals(correlationId, eventFilterSpecification.correlationId());
        assertEquals(Set.of(parameterDefinition), eventFilterSpecification.parameters());
    }

    @Test
    void testNullEventName() {
        CorrelationId correlationId = new CorrelationId(0);
        Exception exception =
                assertThrows(
                        NullPointerException.class,
                        () -> new EventFilterSpecification(null, correlationId, Set.of()));

        assertEquals("Event name cannot be null", exception.getMessage());
    }

    @Test
    void testEventFilterSpecificationWithEmptyParameters() {
        CorrelationId correlationId = new CorrelationId(0);
        EventName name = new EventName("Test");

        Exception exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> new EventFilterSpecification(name, correlationId, Set.of()));

        assertEquals("Parameters cannot be empty", exception.getMessage());
    }

    @Test
    void testEventFilterSpecificationWithNullParameters() {
        CorrelationId correlationId = new CorrelationId(0);
        EventName name = new EventName("Test");

        Exception exception =
                assertThrows(
                        NullPointerException.class,
                        () -> new EventFilterSpecification(name, correlationId, null));

        assertEquals("Parameters cannot be null", exception.getMessage());
    }

    @Test
    void testGetEventSignature() {
        CorrelationId correlationId = new CorrelationId(0);
        EventName name = new EventName("Test");
        ParameterDefinition parameterDefinition = new BoolParameterDefinition(0, false);

        EventFilterSpecification eventFilterSpecification =
                new EventFilterSpecification(name, correlationId, Set.of(parameterDefinition));

        String expectedSignature = "(bool)";
        assertEquals(expectedSignature, eventFilterSpecification.getEventSignature());
    }

    @Test
    void testGetEventSignatureWithMultipleParameters() {
        CorrelationId correlationId = new CorrelationId(1);
        ParameterDefinition parameterDefinition1 = new BoolParameterDefinition(0, false);
        ParameterDefinition parameterDefinition2 = new AddressParameterDefinition(1, true);
        ParameterDefinition parameterDefinition3 = new UintParameterDefinition(256, 2, false);
        ParameterDefinition parameterDefinition4 = new IntParameterDefinition(256, 3, false);
        ParameterDefinition parameterDefinition5 = new BytesFixedParameterDefinition(32, 4, false);
        ParameterDefinition parameterDefinition6 = new ArrayParameterDefinition(5, parameterDefinition1, 5);
        ParameterDefinition parameterDefinition7 = new StructParameterDefinition(6, Set.of(parameterDefinition1, parameterDefinition2));
        ParameterDefinition parameterDefinition8 = new StringParameterDefinition(7);
        ParameterDefinition parameterDefinition9 = new BytesParameterDefinition(8);

        EventName name = new EventName("Test");

        EventFilterSpecification eventFilterSpecification =
                new EventFilterSpecification(
                        name, correlationId, Set.of(parameterDefinition1, parameterDefinition2,
                                parameterDefinition3, parameterDefinition4, parameterDefinition5,
                                parameterDefinition6, parameterDefinition7, parameterDefinition8,
                                parameterDefinition9));

        String expectedSignature = "(bool,address,uint256,int256,bytes32,bool[5],(bool,address),string,bytes)";
        assertEquals(expectedSignature, eventFilterSpecification.getEventSignature());
    }
}
