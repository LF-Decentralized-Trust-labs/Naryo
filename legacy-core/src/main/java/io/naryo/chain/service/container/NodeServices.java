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

package io.naryo.chain.service.container;

import io.naryo.chain.service.BlockchainService;
import io.naryo.chain.service.HederaService;
import io.naryo.chain.service.strategy.BlockSubscriptionStrategy;
import lombok.Data;
import org.web3j.protocol.Web3j;

@Data
public class NodeServices {

    private String nodeName;

    private String nodeType;

    private Web3j web3j;

    private BlockchainService blockchainService;

    private BlockSubscriptionStrategy blockSubscriptionStrategy;

    private HederaService hederaService;
}
