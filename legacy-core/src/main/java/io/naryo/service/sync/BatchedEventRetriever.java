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

package io.naryo.service.sync;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Consumer;

import io.naryo.chain.service.container.ChainServicesContainer;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.filter.ContractEventFilter;
import io.naryo.settings.NaryoSettings;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BatchedEventRetriever implements EventRetriever {

    private ChainServicesContainer servicesContainer;

    private NaryoSettings settings;

    @Override
    public void retrieveEvents(
            ContractEventFilter eventFilter,
            BigInteger startBlock,
            BigInteger endBlock,
            Consumer<List<ContractEventDetails>> eventConsumer) {

        BigInteger batchStartBlock = startBlock;

        while (batchStartBlock.compareTo(endBlock) < 0) {
            BigInteger batchEndBlock;

            if (batchStartBlock.add(settings.getSyncBatchSize()).compareTo(endBlock) >= 0) {
                batchEndBlock = endBlock;
            } else {
                batchEndBlock = batchStartBlock.add(settings.getSyncBatchSize());
            }

            final List<ContractEventDetails> events =
                    servicesContainer
                            .getNodeServices(eventFilter.getNode())
                            .getBlockchainService()
                            .retrieveEvents(eventFilter, batchStartBlock, batchEndBlock);

            eventConsumer.accept(events);

            batchStartBlock = batchEndBlock.add(BigInteger.ONE);
        }
    }
}
