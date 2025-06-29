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

import java.util.List;
import java.util.Optional;

import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.message.MessageDetails;
import io.naryo.integration.eventstore.EventStore;
import io.naryo.model.LatestBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * @author Craig Williams craig.williams@consensys.net
 */
@Component
public class DefaultEventStoreService implements EventStoreService {

    private EventStore eventStore;

    public DefaultEventStoreService(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public Optional<ContractEventDetails> getLatestContractEvent(
            String eventSignature, String contractAddress) {
        int page = eventStore.isPagingZeroIndexed() ? 0 : 1;

        final PageRequest pagination =
                PageRequest.of(page, 1, Sort.by(Sort.Direction.DESC, "blockNumber"));

        final Page<ContractEventDetails> eventsPage =
                eventStore.getContractEventsForSignature(
                        eventSignature, contractAddress, pagination);

        if (eventsPage == null) {
            return Optional.empty();
        }

        final List<ContractEventDetails> events = eventsPage.getContent();

        if (events.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(events.getFirst());
    }

    @Override
    public Optional<LatestBlock> getLatestBlock(String nodeName) {

        return eventStore.getLatestBlockForNode(nodeName);
    }

    @Override
    public Optional<MessageDetails> getLatestMessageFromTopic(String nodeName, String topicId) {
        return eventStore.getLatestMessageFromTopic(nodeName, topicId);
    }
}
