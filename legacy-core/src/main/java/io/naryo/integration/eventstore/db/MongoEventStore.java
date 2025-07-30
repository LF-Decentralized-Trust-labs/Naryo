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

package io.naryo.integration.eventstore.db;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.message.MessageDetails;
import io.naryo.integration.eventstore.SaveableEventStore;
import io.naryo.integration.eventstore.db.repository.ContractEventDetailsRepository;
import io.naryo.integration.eventstore.db.repository.LatestBlockRepository;
import io.naryo.integration.eventstore.db.repository.MessageDetailsRepository;
import io.naryo.model.LatestBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * A saveable event store that stores contract events in a db repository.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
public class MongoEventStore implements SaveableEventStore {

    private final ContractEventDetailsRepository eventDetailsRepository;

    private final MessageDetailsRepository messageDetailsRepository;

    private final LatestBlockRepository latestBlockRepository;

    private final MongoTemplate mongoTemplate;

    public MongoEventStore(
            ContractEventDetailsRepository eventDetailsRepository,
            MessageDetailsRepository messageDetailsRepository,
            LatestBlockRepository latestBlockRepository,
            MongoTemplate mongoTemplate) {
        this.messageDetailsRepository = messageDetailsRepository;
        this.eventDetailsRepository = eventDetailsRepository;
        this.latestBlockRepository = latestBlockRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<ContractEventDetails> getContractEventsForSignature(
            String eventSignature, String contractAddress, PageRequest pagination) {

        final Query query =
                new Query(
                                Criteria.where("eventSpecificationSignature")
                                        .is(eventSignature)
                                        .and("address")
                                        .is(contractAddress))
                        .with(Sort.by(Direction.DESC, "blockNumber"))
                        .collation(Collation.of("en").numericOrderingEnabled());

        final long totalResults = mongoTemplate.count(query, ContractEventDetails.class);

        // Set pagination on a query
        query.skip((long) pagination.getPageNumber() * pagination.getPageSize())
                .limit(pagination.getPageSize());

        final List<ContractEventDetails> results =
                mongoTemplate.find(query, ContractEventDetails.class);

        return new PageImpl<>(results, pagination, totalResults);
    }

    @Override
    public Optional<LatestBlock> getLatestBlockForNode(String nodeName) {
        return latestBlockRepository.findById(nodeName);
    }

    @Override
    public boolean isPagingZeroIndexed() {
        return true;
    }

    @Override
    public Optional<MessageDetails> getLatestMessageFromTopic(String nodeName, String topicId) {
        final Query query =
                new Query(Criteria.where("topicId").is(topicId).and("nodeName").is(nodeName))
                        .with(Sort.by(Direction.DESC, "timestamp"))
                        .collation(Collation.of("en").numericOrderingEnabled());
        final MessageDetails result = mongoTemplate.findOne(query, MessageDetails.class);
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<ContractEventDetails> getContractEvent(
            String eventSignature,
            String contractAddress,
            String blockHash,
            String transactionHash,
            BigInteger logIndex) {
        final Query query =
                new Query(
                                Criteria.where("eventSignature")
                                        .is(eventSignature)
                                        .and("address")
                                        .is(contractAddress)
                                        .and("blockHash")
                                        .is(blockHash)
                                        .and("transactionHash")
                                        .is(transactionHash)
                                        .and("logIndex")
                                        .is(logIndex))
                        .with(Sort.by(Direction.DESC, "timestamp"))
                        .collation(Collation.of("en").numericOrderingEnabled());
        final ContractEventDetails result =
                mongoTemplate.findOne(query, ContractEventDetails.class);
        return result != null ? Optional.of(result) : Optional.empty();
    }

    @Override
    public void save(ContractEventDetails contractEventDetails) {
        eventDetailsRepository.save(contractEventDetails);
    }

    @Override
    public void save(LatestBlock latestBlock) {
        latestBlockRepository.save(latestBlock);
    }

    @Override
    public void save(MessageDetails messageDetails) {
        messageDetailsRepository.save(messageDetails);
    }
}
