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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.naryo.chain.block.BlockListener;
import io.naryo.chain.contract.ContractEventListener;
import io.naryo.chain.service.BlockchainService;
import io.naryo.chain.service.container.ChainServicesContainer;
import io.naryo.chain.service.container.NodeServices;
import io.naryo.chain.service.strategy.BlockSubscriptionStrategy;
import io.naryo.chain.settings.NodeType;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.integration.broadcast.internal.NaryoEventBroadcaster;
import io.naryo.model.FilterSubscription;
import io.naryo.repository.ContractEventFilterRepository;
import io.naryo.service.exception.NotFoundException;
import io.naryo.service.sync.EventSyncService;
import io.naryo.utils.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Craig Williams craig.williams@consensys.net
 */
@Slf4j
@Component
public class DefaultSubscriptionService implements SubscriptionService {

    private final ChainServicesContainer chainServices;
    private final ContractEventFilterRepository eventFilterRepository;
    private final NaryoEventBroadcaster naryoEventBroadcaster;
    private final List<BlockListener> blockListeners;
    private final RetryTemplate retryTemplate;
    private final Map<String, ContractEventFilter> filterSubscriptions;
    private final List<ContractEventListener> contractEventListeners;
    private final EventSyncService eventSyncService;
    private SubscriptionServiceState state = SubscriptionServiceState.UNINITIALISED;

    @Autowired
    public DefaultSubscriptionService(
            ChainServicesContainer chainServices,
            ContractEventFilterRepository eventFilterRepository,
            NaryoEventBroadcaster naryoEventBroadcaster,
            List<BlockListener> blockListeners,
            List<ContractEventListener> contractEventListeners,
            @Qualifier("eternalRetryTemplate") RetryTemplate retryTemplate,
            EventSyncService eventSyncService) {
        this.contractEventListeners = contractEventListeners;
        this.chainServices = chainServices;
        this.eventFilterRepository = eventFilterRepository;
        this.naryoEventBroadcaster = naryoEventBroadcaster;
        this.blockListeners = blockListeners;
        this.retryTemplate = retryTemplate;
        this.eventSyncService = eventSyncService;

        filterSubscriptions =
                StreamSupport.stream(eventFilterRepository.findAll().spliterator(), false)
                        .collect(Collectors.toMap(ContractEventFilter::getId, filter -> filter));
    }

    public void init(List<ContractEventFilter> initFilters) {

        if (initFilters != null && !initFilters.isEmpty()) {
            final List<ContractEventFilter> filtersWithStartBlock =
                    initFilters.stream().filter(filter -> filter.getStartBlock() != null).toList();

            if (!filtersWithStartBlock.isEmpty()) {
                state = SubscriptionServiceState.SYNCING_EVENTS;
                eventSyncService.sync(filtersWithStartBlock);
            }
        }

        chainServices
                .getNodeNames()
                .forEach(
                        nodeName -> {
                            final NodeServices nodeServices =
                                    chainServices.getNodeServices(nodeName);
                            final String nodeType = nodeServices.getNodeType();
                            if (nodeType.equals(NodeType.NORMAL.name())
                                    || nodeType.equals(NodeType.MIRROR.name())) {
                                subscribeToNewBlockEvents(
                                        nodeServices.getBlockSubscriptionStrategy(),
                                        blockListeners);
                            }
                        });

        state = SubscriptionServiceState.SUBSCRIBED;
    }

    @Override
    public ContractEventFilter registerContractEventFilter(
            ContractEventFilter filter, boolean broadcast) {
        return doRegisterContractEventFilter(filter, broadcast);
    }

    @Override
    @Async
    public Future<ContractEventFilter> registerContractEventFilterWithRetries(
            ContractEventFilter filter, boolean broadcast) {
        return CompletableFuture.completedFuture(
                retryTemplate.execute(context -> doRegisterContractEventFilter(filter, broadcast)));
    }

    @Override
    public List<ContractEventFilter> listContractEventFilters() {
        return new ArrayList<>(filterSubscriptions.values());
    }

    @Override
    public void unregisterContractEventFilter(String filterId) throws NotFoundException {
        unregisterContractEventFilter(filterId, true);
    }

    @Override
    public void unregisterContractEventFilter(String filterId, boolean broadcast)
            throws NotFoundException {
        final ContractEventFilter filterToUnregister = getRegisteredFilter(filterId);

        if (filterToUnregister == null) {
            throw new NotFoundException(
                    String.format("Filter with id %s, doesn't exist", filterId));
        }

        deleteContractEventFilter(filterToUnregister);
        removeFilterSubscription(filterId);

        if (broadcast) {
            broadcastContractEventFilterRemoved(filterToUnregister);
        }
    }

    @Override
    public void unsubscribeToAllSubscriptions(String nodeName) {
        filterSubscriptions
                .entrySet()
                .removeIf(entry -> entry.getValue().getNode().equals(nodeName));
    }

    @Override
    public SubscriptionServiceState getState() {
        return state;
    }

    private ContractEventFilter doRegisterContractEventFilter(
            ContractEventFilter filter, boolean broadcast) {
        try {
            populateIdIfMissing(filter);
            boolean isAlreadyRegistered = isFilterRegistered(filter);

            if (!isAlreadyRegistered) {
                log.info("Registering filter: {}", JSON.stringify(filter));

                if (filter.getStartBlock() != null) {
                    final FilterSubscription sub = registerContractEventFilter(filter);

                    if (sub != null) {
                        filter.setStartBlock(sub.getStartBlock());
                    }
                }
            }

            filter = saveContractEventFilter(filter);
            filterSubscriptions.put(filter.getId(), filter);

            if (isAlreadyRegistered) {
                log.info("Updated contract event filter with id: {}", filter.getId());
            } else if (broadcast) {
                broadcastContractEventFilterAdded(filter);

                log.debug("Registered filters: {}", JSON.stringify(filterSubscriptions));
            }
            return filter;
        } catch (Exception e) {
            log.error("Error registering filter {}", filter.getId(), e);
            throw e;
        }
    }

    private void subscribeToNewBlockEvents(
            BlockSubscriptionStrategy subscriptionStrategy, List<BlockListener> blockListeners) {
        blockListeners.forEach(subscriptionStrategy::addBlockListener);

        subscriptionStrategy.subscribe();
    }

    private ContractEventFilter saveContractEventFilter(ContractEventFilter contractEventFilter) {
        return eventFilterRepository.save(contractEventFilter);
    }

    private void deleteContractEventFilter(ContractEventFilter contractEventFilter) {
        eventFilterRepository.deleteById(contractEventFilter.getId());
    }

    private void broadcastContractEventFilterAdded(ContractEventFilter filter) {
        naryoEventBroadcaster.broadcastEventFilterAdded(filter);
    }

    private void broadcastContractEventFilterRemoved(ContractEventFilter filter) {
        naryoEventBroadcaster.broadcastEventFilterRemoved(filter);
    }

    private boolean isFilterRegistered(ContractEventFilter contractEventFilter) {
        return (getRegisteredFilter(contractEventFilter.getId()) != null);
    }

    private FilterSubscription registerContractEventFilter(ContractEventFilter filter) {
        final NodeServices nodeServices = chainServices.getNodeServices(filter.getNode());

        if (nodeServices == null) {
            log.warn("No node configured with name {}, not registering filter", filter.getNode());
            return null;
        }

        final BlockchainService blockchainService = nodeServices.getBlockchainService();

        return blockchainService.registerEventListener(
                filter,
                contractEvent ->
                        contractEventListeners.forEach(
                                listener -> triggerListener(listener, contractEvent)));
    }

    private void triggerListener(
            ContractEventListener listener, ContractEventDetails contractEventDetails) {
        try {
            listener.onEvent(contractEventDetails);
        } catch (RuntimeException t) {
            log.error(
                    "An error occurred when processing contractEvent with id {}",
                    contractEventDetails.getEventIdentifier(),
                    t);
        }
    }

    private ContractEventFilter getRegisteredFilter(String filterId) {
        return filterSubscriptions.get(filterId);
    }

    private void removeFilterSubscription(String filterId) {
        filterSubscriptions.remove(filterId);
    }

    private void populateIdIfMissing(ContractEventFilter filter) {
        if (filter.getId() == null) {
            filter.setId(UUID.randomUUID().toString());
        }
    }
}
