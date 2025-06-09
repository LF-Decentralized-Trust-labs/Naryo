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

package io.naryo.integration.broadcast.blockchain;

import java.util.Optional;

import io.naryo.dto.block.BlockDetails;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.dto.message.*;
import io.naryo.dto.transaction.TransactionDetails;
import io.naryo.integration.KafkaSettings;
import io.naryo.utils.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * A BlockchainEventBroadcaster that broadcasts the events to a Kafka queue.
 *
 * <p>The key for each message will be defined by the correlationIdStrategy if configured, or a
 * combination of the transactionHash, blockHash and logIndex otherwise.
 *
 * <p>The topic names for block and contract events can be configured via the
 * kafka.topic.contractEvents and kafka.topic.blockEvents properties.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@Slf4j
public class KafkaBlockchainEventBroadcaster implements BlockchainEventBroadcaster {

    private final KafkaTemplate<String, NaryoMessage> kafkaTemplate;
    private final KafkaSettings kafkaSettings;
    private final CrudRepository<ContractEventFilter, String> filterRepository;

    public KafkaBlockchainEventBroadcaster(
            KafkaTemplate<String, NaryoMessage> kafkaTemplate,
            KafkaSettings kafkaSettings,
            CrudRepository<ContractEventFilter, String> filterRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaSettings = kafkaSettings;
        this.filterRepository = filterRepository;
    }

    @Override
    public void broadcastNewBlock(BlockDetails block) {
        final NaryoMessage<BlockDetails> message = createBlockEventMessage(block);
        log.info("Sending block message: ${}", JSON.stringify(message));

        kafkaTemplate.send(kafkaSettings.getBlockEventsTopic(), message.getId(), message);
    }

    @Override
    public void broadcastContractEvent(ContractEventDetails eventDetails) {
        final NaryoMessage<ContractEventDetails> message = createContractEventMessage(eventDetails);
        log.info("Sending contract event message: {}", JSON.stringify(message));

        kafkaTemplate.send(
                kafkaSettings.getContractEventsTopic(),
                getContractEventCorrelationId(message),
                message);
    }

    @Override
    public void broadcastTransaction(TransactionDetails transactionDetails) {
        final NaryoMessage<TransactionDetails> message =
                createTransactionEventMessage(transactionDetails);
        log.info("Sending transaction event message: {}", JSON.stringify(message));

        kafkaTemplate.send(
                kafkaSettings.getTransactionEventsTopic(),
                transactionDetails.getBlockHash(),
                message);
    }

    @Override
    public void broadcastMessage(MessageDetails messageDetails) {
        final NaryoMessage<MessageDetails> message = createMessageEventMessage(messageDetails);
        log.info("Sending event message: {}", JSON.stringify(message));

        kafkaTemplate.send(
                kafkaSettings.getMessageEventsTopic(), messageDetails.getTopicId(), message);
    }

    protected NaryoMessage<BlockDetails> createBlockEventMessage(BlockDetails blockDetails) {
        return new BlockEvent(blockDetails);
    }

    protected NaryoMessage<ContractEventDetails> createContractEventMessage(
            ContractEventDetails contractEventDetails) {
        return new ContractEvent(contractEventDetails);
    }

    protected NaryoMessage<TransactionDetails> createTransactionEventMessage(
            TransactionDetails transactionDetails) {
        return new TransactionEvent(transactionDetails);
    }

    protected NaryoMessage<MessageDetails> createMessageEventMessage(
            MessageDetails messageDetails) {
        return new MessageEvent(messageDetails);
    }

    private String getContractEventCorrelationId(NaryoMessage<ContractEventDetails> message) {
        final Optional<ContractEventFilter> filter =
                filterRepository.findById(message.getDetails().getFilterId());

        if (!filter.isPresent() || filter.get().getCorrelationIdStrategy() == null) {
            return message.getId();
        }

        return filter.get().getCorrelationIdStrategy().getCorrelationId(message.getDetails());
    }
}
