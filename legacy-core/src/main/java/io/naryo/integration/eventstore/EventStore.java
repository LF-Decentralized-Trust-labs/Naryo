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

package io.naryo.integration.eventstore;

import java.math.BigInteger;
import java.util.Optional;

import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.message.MessageDetails;
import io.naryo.model.LatestBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * Interface for integrating with an event store, in order to obtain events for a specified
 * signature.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
public interface EventStore {
    Page<ContractEventDetails> getContractEventsForSignature(
            String eventSignature, String contractAddress, PageRequest pagination);

    Optional<LatestBlock> getLatestBlockForNode(String nodeName);

    boolean isPagingZeroIndexed();

    Optional<MessageDetails> getLatestMessageFromTopic(String nodeName, String topicId);

    Optional<ContractEventDetails> getContractEvent(
            String eventSignature,
            String contractAddress,
            String blockHash,
            String transactionHash,
            BigInteger logIndex);
}
