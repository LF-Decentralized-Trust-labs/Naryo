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

package io.naryo.integration.eventstore.rest.client;

import io.naryo.dto.event.ContractEventDetails;
import io.naryo.model.LatestBlock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@ConditionalOnProperty(name = "eventStore.type", havingValue = "REST")
@FeignClient(name = "eventStore", url = "${eventStore.url}")
public interface EventStoreClient {

    @GetMapping(value = "${eventStore.eventPath}")
    Page<ContractEventDetails> getContractEvents(
            @RequestParam(value = "page") int pageNo,
            @RequestParam(value = "size") int pageSize,
            @RequestParam(value = "sort") String sortAttribute,
            @RequestParam(value = "dir") Sort.Direction sortDirection,
            @RequestParam(value = "signature") String signature,
            @RequestParam(value = "contractAddress") String contractAddress);

    @GetMapping(value = "${eventStore.latestBlockPath}")
    LatestBlock getLatestBlock(@RequestParam(value = "nodeName") String nodeName);
}
