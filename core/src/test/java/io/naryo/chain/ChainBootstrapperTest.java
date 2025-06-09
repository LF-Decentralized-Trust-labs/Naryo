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

package io.naryo.chain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import io.naryo.chain.config.EventFilterConfiguration;
import io.naryo.chain.config.TransactionFilterConfiguration;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.factory.ContractEventFilterFactory;
import io.naryo.model.TransactionMonitoringSpec;
import io.naryo.repository.ContractEventFilterRepository;
import io.naryo.repository.TransactionMonitoringSpecRepository;
import io.naryo.service.SubscriptionService;
import io.naryo.service.TransactionMonitoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChainBootstrapperTest {
    @Mock private EventFilterConfiguration mockConfig;
    @Mock private SubscriptionService mockSubscriptionService;
    @Mock private TransactionMonitoringService mockTransactionMonitoringService;
    @Mock private ContractEventFilterRepository mockFilterRepository;
    @Mock private TransactionMonitoringSpecRepository mockTransactionMonitoringRepository;
    @Mock private ContractEventFilterFactory mockFilterFactory;
    @Mock private TransactionFilterConfiguration transactionFilterConfiguration;
    private ChainBootstrapper underTest;

    @BeforeEach
    public void init() {
        underTest =
                new ChainBootstrapper(
                        mockSubscriptionService,
                        mockTransactionMonitoringService,
                        mockConfig,
                        mockFilterRepository,
                        mockTransactionMonitoringRepository,
                        Optional.of(Collections.singletonList(mockFilterFactory)),
                        transactionFilterConfiguration);
    }

    @Test
    void testThatEventFiltersAreRegistered() {
        final List<ContractEventFilter> mockConfiguredFilters =
                Arrays.asList(mock(ContractEventFilter.class), mock(ContractEventFilter.class));
        final List<ContractEventFilter> mockFilterFactoryFilters =
                Arrays.asList(mock(ContractEventFilter.class), mock(ContractEventFilter.class));

        when(mockConfig.getConfiguredEventFilters()).thenReturn(mockConfiguredFilters);
        when(mockFilterFactory.build()).thenReturn(mockFilterFactoryFilters);

        doBootstrap();

        verify(mockSubscriptionService, times(1))
                .registerContractEventFilterWithRetries(mockConfiguredFilters.getFirst(), true);
        verify(mockSubscriptionService, times(1))
                .registerContractEventFilterWithRetries(mockConfiguredFilters.get(1), true);
        verify(mockSubscriptionService, times(1))
                .registerContractEventFilterWithRetries(mockFilterFactoryFilters.getFirst(), true);
        verify(mockSubscriptionService, times(1))
                .registerContractEventFilterWithRetries(mockFilterFactoryFilters.get(1), true);
    }

    @Test
    void testThatAlreadyExistingEventFiltersAreRemoved() {
        ContractEventFilter contractEventFilter = new ContractEventFilter();
        contractEventFilter.setId("id1");

        final List<ContractEventFilter> mockExistingEventFilters =
                Arrays.asList(contractEventFilter, mock(ContractEventFilter.class));
        final List<ContractEventFilter> mockConfiguredFilters =
                Collections.singletonList(contractEventFilter);
        final List<ContractEventFilter> mockFilterFactoryFilters =
                Arrays.asList(mock(ContractEventFilter.class), mock(ContractEventFilter.class));

        when(mockConfig.getConfiguredEventFilters()).thenReturn(mockConfiguredFilters);
        when(mockFilterRepository.findAll()).thenReturn(mockExistingEventFilters);
        when(mockFilterFactory.build()).thenReturn(mockFilterFactoryFilters);

        doBootstrap();

        verify(mockSubscriptionService, times(1))
                .registerContractEventFilterWithRetries(mockConfiguredFilters.getFirst(), true);
        verify(mockSubscriptionService, times(0))
                .registerContractEventFilterWithRetries(mockExistingEventFilters.getFirst(), false);
        verify(mockSubscriptionService, times(1))
                .registerContractEventFilterWithRetries(mockExistingEventFilters.get(1), false);
        verify(mockSubscriptionService, times(1))
                .registerContractEventFilterWithRetries(mockFilterFactoryFilters.getFirst(), true);
        verify(mockSubscriptionService, times(1))
                .registerContractEventFilterWithRetries(mockFilterFactoryFilters.get(1), true);
    }

    @Test
    void testThatTransactionsMonitorsAreRegistered() {

        final List<TransactionMonitoringSpec> mockMonitorSpecs =
                Arrays.asList(
                        mock(TransactionMonitoringSpec.class),
                        mock(TransactionMonitoringSpec.class));

        when(mockTransactionMonitoringRepository.findAll()).thenReturn(mockMonitorSpecs);

        doBootstrap();

        verify(mockTransactionMonitoringService, times(1))
                .registerTransactionsToMonitor(mockMonitorSpecs.getFirst(), true);
        verify(mockTransactionMonitoringService, times(1))
                .registerTransactionsToMonitor(mockMonitorSpecs.get(1), true);
    }

    @Test
    void testThatContractTransactionFiltersAreRegistered() {

        final List<TransactionMonitoringSpec> mockConfiguredFilters =
                Arrays.asList(
                        mock(TransactionMonitoringSpec.class),
                        mock(TransactionMonitoringSpec.class));

        when(transactionFilterConfiguration.getConfiguredTransactionFilters())
                .thenReturn(mockConfiguredFilters);

        doBootstrap();

        verify(mockTransactionMonitoringService, times(1))
                .registerTransactionsToMonitor(mockConfiguredFilters.getFirst(), true);
        verify(mockTransactionMonitoringService, times(1))
                .registerTransactionsToMonitor(mockConfiguredFilters.get(1), true);
    }

    private void doBootstrap() {
        underTest.onApplicationEvent(null);
    }
}
