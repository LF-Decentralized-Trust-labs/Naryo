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

import java.util.Optional;

import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.message.MessageDetails;
import io.naryo.model.LatestBlock;

/**
 * A service that interacts with the event store in order to retrieve data required by Naryo.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
public interface EventStoreService {

    /**
     * Returns the contract event with the latest block, that matches the event signature.
     *
     * @param eventSignature The event signature
     * @param contractAddress The event contract address
     * @return The event details
     */
    Optional<ContractEventDetails> getLatestContractEvent(
            String eventSignature, String contractAddress);

    /**
     * Returns the latest block, for the specified node.
     *
     * @param nodeName The nodename
     * @return The block details
     */
    Optional<LatestBlock> getLatestBlock(String nodeName);

    /**
     * Returns the latest message, for the specified node
     *
     * @param node The node name
     * @param topicId The topic ID
     * @return The message details
     */
    Optional<MessageDetails> getLatestMessageFromTopic(String node, String topicId);
}
