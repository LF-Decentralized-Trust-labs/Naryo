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

package io.naryo.chain.contract;

import io.naryo.dto.event.ContractEventDetails;
import io.naryo.integration.eventstore.EventStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * A contract event listener that logs the contract event details.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@Slf4j
@Component
public class LoggingContractEventListener extends BaseContractEventListener {

    public LoggingContractEventListener(EventStore eventStore) {
        super(eventStore);
    }

    @Override
    public void onEvent(ContractEventDetails eventDetails) {
        if (isExistingEvent(eventDetails)) {
            log.info("Contract event fired: {}", eventDetails.getName());
        }
    }
}
