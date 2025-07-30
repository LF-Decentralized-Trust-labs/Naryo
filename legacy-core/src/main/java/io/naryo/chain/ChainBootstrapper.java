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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.naryo.chain.config.EventFilterConfiguration;
import io.naryo.chain.config.TransactionFilterConfiguration;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.factory.ContractEventFilterFactory;
import io.naryo.model.TransactionMonitoringSpec;
import io.naryo.service.SubscriptionService;
import io.naryo.service.TransactionMonitoringService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Registers filters that are either configured within the properties file, exist in the Naryo
 * database on startup, or are returned from ContractEventFilterFactory beans.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@Slf4j
@Service
@AllArgsConstructor
public class ChainBootstrapper {

    private SubscriptionService subscriptionService;
    private TransactionMonitoringService transactionMonitoringService;
    private EventFilterConfiguration filterConfiguration;
    private CrudRepository<ContractEventFilter, String> filterRepository;
    private CrudRepository<TransactionMonitoringSpec, String> transactionMonitoringRepository;
    private Optional<List<ContractEventFilterFactory>> contractEventFilterFactories;
    private TransactionFilterConfiguration transactionFilterConfiguration;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent ignored) {
        registerTransactionsToMonitor(transactionMonitoringRepository.findAll());
        registerTransactionsToMonitor(
                transactionFilterConfiguration.getConfiguredTransactionFilters());

        // Remove from existing eventFilters the ones that are included by configuration to avoid
        // overwriting
        List<ContractEventFilter> existingEventFilters =
                StreamSupport.stream(filterRepository.findAll().spliterator(), false)
                        .collect(Collectors.toList());
        List<ContractEventFilter> configuredEventFilters =
                filterConfiguration.getConfiguredEventFilters();
        existingEventFilters.removeAll(configuredEventFilters);

        subscriptionService.init(configuredEventFilters);

        registerFilters(configuredEventFilters, true);
        registerFilters(existingEventFilters, false);

        contractEventFilterFactories.ifPresent(
                factories -> factories.forEach(factory -> registerFilters(factory.build(), true)));
    }

    private void registerFilters(Iterable<ContractEventFilter> filters, boolean broadcast) {
        if (filters != null) {
            filters.forEach(filter -> registerFilter(filter, broadcast));
        }
    }

    private void registerFilter(ContractEventFilter filter, boolean broadcast) {
        subscriptionService.registerContractEventFilterWithRetries(filter, broadcast);
    }

    private void registerTransactionsToMonitor(Iterable<TransactionMonitoringSpec> specs) {
        if (specs != null) {
            specs.forEach(this::registerTransactionToMonitor);
        }
    }

    private void registerTransactionToMonitor(TransactionMonitoringSpec spec) {
        transactionMonitoringService.registerTransactionsToMonitor(spec, true);
    }
}
