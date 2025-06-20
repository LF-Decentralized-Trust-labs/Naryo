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

import io.naryo.chain.service.block.EventBlockManagementService;
import io.naryo.dto.event.ContractEventDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A contract event listener that updates the latest block number seen for the event spec.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@Component
public class EventBlockUpdaterListener implements ContractEventListener {

    private EventBlockManagementService blockManagement;

    @Autowired
    public EventBlockUpdaterListener(EventBlockManagementService blockManagement) {
        this.blockManagement = blockManagement;
    }

    @Override
    public void onEvent(ContractEventDetails eventDetails) {
        blockManagement.updateLatestBlock(
                eventDetails.getEventSpecificationSignature(),
                eventDetails.getBlockNumber(),
                eventDetails.getAddress());
    }
}
