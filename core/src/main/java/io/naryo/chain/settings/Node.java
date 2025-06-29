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

package io.naryo.chain.settings;

import java.math.BigInteger;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node {

    private String name;
    private NodeType nodeType;
    private ChainType chainType;
    private String url;
    private Map<String, String> headers;
    private BigInteger limitPerRequest = BigInteger.valueOf(100);
    private BigInteger callRetries;
    private Long pollingInterval;
    private String username;
    private String password;
    private BlockStrategy blockStrategy;
    private Boolean addTransactionRevertReason;
    private Integer maxIdleConnections;
    private Long keepAliveDuration;
    private Long connectionTimeout;
    private Long readTimeout;
    private Integer syncingThreshold;
    private Long healthcheckInterval;
    private BigInteger blocksToWaitForConfirmation;
    private BigInteger blocksToWaitForMissingTx;
    private BigInteger blocksToWaitBeforeInvalidating;
    private BigInteger initialStartBlock;
    private BigInteger numBlocksToReplay;
    private BigInteger maxBlocksToSync;
    private Map<String, Object> extension;
    private BigInteger limitMirrorNodeResults;
}
