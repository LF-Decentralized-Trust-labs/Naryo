package io.naryo.utils;

import java.util.Arrays;
import java.util.UUID;

import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.event.filter.ContractEventSpecification;
import io.naryo.dto.event.filter.ParameterDefinition;
import io.naryo.dto.event.filter.ParameterType;
import io.naryo.service.SubscriptionService;
import io.naryo.service.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventFilterCreator {

    private final SubscriptionService subscriptionService;

    @Autowired
    public EventFilterCreator(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public void createFilter(ContractEventFilter filter) {
        subscriptionService.registerContractEventFilter(filter, true);
        log.info("Created filter: {}", filter);
    }

    public void removeFilter(ContractEventFilter filter) throws NotFoundException {
        subscriptionService.unregisterContractEventFilter(filter.getId());
        log.info("Removed filter: {}", filter);
    }

    public static ContractEventFilter buildDummyEventFilter(String contractAddress) {
        return buildDummyEventFilter(String.valueOf(UUID.randomUUID()), contractAddress);
    }

    public static ContractEventFilter buildDummyEventFilter(String id, String contractAddress) {
        final ContractEventSpecification eventSpec = new ContractEventSpecification();
        eventSpec.setIndexedParameterDefinitions(
                Arrays.asList(
                        new ParameterDefinition(0, ParameterType.build("BYTES32")),
                        new ParameterDefinition(1, ParameterType.build("ADDRESS"))));

        eventSpec.setNonIndexedParameterDefinitions(
                Arrays.asList(
                        new ParameterDefinition(2, ParameterType.build("UINT256")),
                        new ParameterDefinition(3, ParameterType.build("STRING")),
                        new ParameterDefinition(4, ParameterType.build("UINT8"))));

        eventSpec.setEventName("DummyEvent");

        return buildFilter(id, contractAddress, eventSpec);
    }

    public static ContractEventFilter buildDummyEventArrayFilter(String contractAddress) {
        return buildDummyEventArrayFilter(String.valueOf(UUID.randomUUID()), contractAddress);
    }

    public static ContractEventFilter buildDummyEventArrayFilter(
            String id, String contractAddress) {
        final ContractEventSpecification eventSpec = new ContractEventSpecification();

        eventSpec.setNonIndexedParameterDefinitions(
                Arrays.asList(
                        new ParameterDefinition(0, ParameterType.build("UINT256[]")),
                        new ParameterDefinition(1, ParameterType.build("BYTES32[]"))));

        eventSpec.setEventName("DummyEventArray");

        return buildFilter(id, contractAddress, eventSpec);
    }

    public static ContractEventFilter buildDummyEventNotOrderedFilter(String contractAddress) {
        return buildDummyEventNotOrderedFilter(String.valueOf(UUID.randomUUID()), contractAddress);
    }

    public static ContractEventFilter buildDummyEventNotOrderedFilter(
            String id, String contractAddress) {
        final ContractEventSpecification eventSpec = new ContractEventSpecification();
        eventSpec.setIndexedParameterDefinitions(
                Arrays.asList(
                        new ParameterDefinition(0, ParameterType.build("BYTES32")),
                        new ParameterDefinition(2, ParameterType.build("ADDRESS"))));

        eventSpec.setNonIndexedParameterDefinitions(
                Arrays.asList(
                        new ParameterDefinition(1, ParameterType.build("UINT256")),
                        new ParameterDefinition(3, ParameterType.build("STRING")),
                        new ParameterDefinition(4, ParameterType.build("UINT8"))));

        eventSpec.setEventName("DummyEventNotOrdered");

        return buildFilter(id, contractAddress, eventSpec);
    }

    private static ContractEventFilter buildFilter(
            String id, String contractAddress, ContractEventSpecification eventSpec) {
        final ContractEventFilter contractEventFilter = new ContractEventFilter();
        contractEventFilter.setId(id);
        contractEventFilter.setContractAddress(contractAddress);
        contractEventFilter.setEventSpecification(eventSpec);

        return contractEventFilter;
    }
}
