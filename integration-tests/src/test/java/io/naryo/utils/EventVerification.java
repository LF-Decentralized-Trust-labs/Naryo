package io.naryo.utils;

import java.math.BigInteger;

import io.naryo.chain.util.Web3jUtil;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.ContractEventStatus;
import io.naryo.dto.event.filter.ContractEventFilter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventVerification {

    public static void verifyDummyEvent(
            ContractEventFilter registeredFilter,
            ContractEventDetails eventDetails,
            ContractEventStatus status,
            String valueOne,
            String valueTwo,
            BigInteger valueThree,
            String valueFour) {
        assertEquals(
                registeredFilter.getEventSpecification().getEventName(), eventDetails.getName());
        assertEquals(status, eventDetails.getStatus());
        assertEquals(valueOne, eventDetails.getIndexedParameters().getFirst().getValue());
        assertEquals(valueTwo, eventDetails.getIndexedParameters().get(1).getValue());
        assertEquals(valueThree, eventDetails.getNonIndexedParameters().getFirst().getValue());
        assertEquals(valueFour, eventDetails.getNonIndexedParameters().get(1).getValue());
        assertEquals(BigInteger.ONE, eventDetails.getNonIndexedParameters().get(2).getValue());
        assertEquals(
                Web3jUtil.getSignature(registeredFilter.getEventSpecification()),
                eventDetails.getEventSpecificationSignature());
        assertNotNull(eventDetails.getTimestamp());
    }
}
