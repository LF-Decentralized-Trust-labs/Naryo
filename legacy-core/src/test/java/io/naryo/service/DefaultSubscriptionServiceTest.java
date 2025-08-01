/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.naryo.service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import io.naryo.chain.block.BlockListener;
import io.naryo.chain.contract.ContractEventListener;
import io.naryo.chain.service.BlockchainService;
import io.naryo.chain.service.container.ChainServicesContainer;
import io.naryo.chain.service.container.NodeServices;
import io.naryo.chain.service.strategy.BlockSubscriptionStrategy;
import io.naryo.chain.settings.NodeType;
import io.naryo.constant.Constants;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.event.filter.ContractEventSpecification;
import io.naryo.dto.event.filter.ParameterDefinition;
import io.naryo.dto.event.filter.ParameterType;
import io.naryo.integration.broadcast.internal.NaryoEventBroadcaster;
import io.naryo.repository.ContractEventFilterRepository;
import io.naryo.service.exception.NotFoundException;
import io.naryo.service.sync.EventSyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.retry.support.RetryTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DefaultSubscriptionServiceTest {
    private static final String FILTER_ID = "123-456";

    private static final String EVENT_NAME = "DummyEvent";

    private static final String CONTRACT_ADDRESS = "0x7a55a28856d43bba3c6a7e36f2cee9a82923e99b";

    private static ContractEventSpecification eventSpec;

    private DefaultSubscriptionService underTest;

    @Mock private ChainServicesContainer mockChainServicesContainer;
    @Mock private NodeServices mockNodeServices;
    @Mock private BlockchainService mockBlockchainService;
    @Mock private BlockSubscriptionStrategy mockBlockSubscriptionStrategy;
    @Mock private ContractEventFilterRepository mockRepo;
    @Mock private NaryoEventBroadcaster mockFilterBroadcaster;
    @Mock private BlockListener mockBlockListener1;
    @Mock private BlockListener mockBlockListener2;
    @Mock private ContractEventListener mockEventListener1;
    @Mock private ContractEventListener mockEventListener2;
    @Mock private EventSyncService mockEventSyncService;

    private RetryTemplate mockRetryTemplate;

    static {
        eventSpec = new ContractEventSpecification();
        eventSpec.setEventName(EVENT_NAME);

        eventSpec.setIndexedParameterDefinitions(
                Arrays.asList(new ParameterDefinition(0, ParameterType.build("UINT256"))));

        eventSpec.setNonIndexedParameterDefinitions(
                Arrays.asList(
                        new ParameterDefinition(1, ParameterType.build("UINT256")),
                        new ParameterDefinition(2, ParameterType.build("ADDRESS"))));
    }

    @BeforeEach
    public void init() {
        when(mockChainServicesContainer.getNodeServices(Constants.DEFAULT_NODE_NAME))
                .thenReturn(mockNodeServices);
        when(mockChainServicesContainer.getNodeNames())
                .thenReturn(Collections.singletonList(Constants.DEFAULT_NODE_NAME));
        when(mockNodeServices.getNodeType()).thenReturn(NodeType.NORMAL.name());
        when(mockNodeServices.getBlockchainService()).thenReturn(mockBlockchainService);
        when(mockNodeServices.getBlockSubscriptionStrategy())
                .thenReturn(mockBlockSubscriptionStrategy);
        mockRetryTemplate = new RetryTemplate();
        when(mockNodeServices.getNodeType()).thenReturn("NORMAL");

        underTest =
                new DefaultSubscriptionService(
                        mockChainServicesContainer,
                        mockRepo,
                        mockFilterBroadcaster,
                        Arrays.asList(mockBlockListener1, mockBlockListener2),
                        Arrays.asList(mockEventListener1, mockEventListener2),
                        mockRetryTemplate,
                        mockEventSyncService);
    }

    @Test
    void testSubscribeToNewBlocksOnInit() {

        underTest.init(null);

        verify(mockBlockSubscriptionStrategy, times(1)).addBlockListener(mockBlockListener1);
        verify(mockBlockSubscriptionStrategy, times(1)).addBlockListener(mockBlockListener2);
    }

    @Test
    void testRegisterNewContractEventFilter() {
        final ContractEventFilter filter = createEventFilter();

        when(mockRepo.save(any())).thenReturn(filter);

        underTest.registerContractEventFilter(filter, true);

        verifyContractEventFilterBroadcast(filter);
        assertEquals(1, underTest.listContractEventFilters().size());
    }

    @Test
    void testRegisterNewContractEventFilterWithSpecificStartBlock() {
        final ContractEventFilter filter = createEventFilter();
        filter.setStartBlock(BigInteger.ONE);

        when(mockRepo.save(any())).thenReturn(filter);

        underTest.registerContractEventFilter(filter, true);

        verifyContractEventFilterBroadcast(filter);
        assertEquals(1, underTest.listContractEventFilters().size());
        assertEquals(
                BigInteger.ONE,
                underTest.listContractEventFilters().stream().findFirst().get().getStartBlock());
    }

    @Test
    void testRegisterNewContractEventFilterBroadcastFalse() {
        final ContractEventFilter filter = createEventFilter();

        when(mockRepo.save(any())).thenReturn(filter);

        underTest.registerContractEventFilter(filter, false);

        assertEquals(1, underTest.listContractEventFilters().size());
    }

    @Test
    void testRegisterNewContractEventFilterAlreadyRegistered() {
        final ContractEventFilter filter = createEventFilter();

        when(mockRepo.save(any())).thenReturn(filter);

        underTest.registerContractEventFilter(filter, true);
        underTest.registerContractEventFilter(filter, true);

        verifyContractEventFilterBroadcast(filter);

        assertEquals(1, underTest.listContractEventFilters().size());
    }

    @Test
    void testRegisterNewContractEventFilterAutoGenerateId() {
        final ContractEventFilter filter = createEventFilter(null);

        when(mockRepo.save(any())).thenReturn(filter);

        underTest.registerContractEventFilter(filter, true);

        assertTrue(!filter.getId().isEmpty());
        assertEquals(1, underTest.listContractEventFilters().size());
    }

    @Test
    void testListContractEventFilterAlreadyRegistered() {
        final ContractEventFilter filter1 = createEventFilter(null);

        when(mockRepo.save(any())).thenReturn(filter1);

        underTest.registerContractEventFilter(filter1, true);
        underTest.registerContractEventFilter(filter1, true);

        assertEquals(1, underTest.listContractEventFilters().size());
    }

    @Test
    void testUnregisterContractEventFilter() throws NotFoundException {
        final ContractEventFilter filter = createEventFilter();

        when(mockRepo.save(any())).thenReturn(filter);

        underTest.registerContractEventFilter(filter, false);

        underTest.unregisterContractEventFilter(FILTER_ID);

        verify(mockRepo, times(1)).deleteById(FILTER_ID);
        verify(mockFilterBroadcaster, times(1)).broadcastEventFilterRemoved(filter);
        assertEquals(0, underTest.listContractEventFilters().size());

        boolean exceptionThrown = false;
        // This will test that the filter has been deleted from memory
        try {
            underTest.unregisterContractEventFilter(FILTER_ID);
        } catch (NotFoundException e) {
            // Expected
            exceptionThrown = true;
        }

        assertEquals(true, exceptionThrown);
    }

    @Test
    void testUnsubscribeToAllSubscriptions() {
        final ContractEventFilter filter1 = createEventFilter("filter1");
        final ContractEventFilter filter2 = createEventFilter();

        when(mockRepo.save(any())).thenReturn(filter1);

        underTest.registerContractEventFilter(filter1, false);
        underTest.registerContractEventFilter(filter2, false);
        underTest.unsubscribeToAllSubscriptions(Constants.DEFAULT_NODE_NAME);

        assertEquals(0, underTest.listContractEventFilters().size());
    }

    private void verifyContractEventFilterBroadcast(ContractEventFilter filter) {
        verify(mockRepo, atLeastOnce()).save(filter);

        verify(mockFilterBroadcaster, times(1)).broadcastEventFilterAdded(filter);
    }

    private ContractEventFilter createEventFilter(String id) {
        final ContractEventFilter filter = new ContractEventFilter();

        filter.setId(id);
        filter.setContractAddress(CONTRACT_ADDRESS);
        filter.setEventSpecification(eventSpec);
        filter.setNode(Constants.DEFAULT_NODE_NAME);

        return filter;
    }

    private ContractEventFilter createEventFilter() {
        return createEventFilter(FILTER_ID);
    }
}
