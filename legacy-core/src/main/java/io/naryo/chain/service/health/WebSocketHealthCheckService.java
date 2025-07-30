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

package io.naryo.chain.service.health;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import io.naryo.chain.service.BlockchainException;
import io.naryo.chain.service.BlockchainService;
import io.naryo.chain.service.health.strategy.ReconnectionStrategy;
import io.naryo.chain.service.strategy.BlockSubscriptionStrategy;
import io.naryo.monitoring.NaryoValueMonitor;
import io.naryo.service.EventStoreService;
import io.naryo.service.SubscriptionService;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.websocket.NaryoWebSocketService;
import org.web3j.protocol.websocket.WebSocketClient;

public class WebSocketHealthCheckService extends NodeHealthCheckService {

    private final WebSocketClient webSocketClient;

    public WebSocketHealthCheckService(
            Web3jService web3jService,
            BlockchainService blockchainService,
            BlockSubscriptionStrategy blockSubscription,
            ReconnectionStrategy failureListener,
            SubscriptionService subscriptionService,
            NaryoValueMonitor valueMonitor,
            EventStoreService eventStoreService,
            Integer syncingThreshold,
            ScheduledThreadPoolExecutor taskScheduler,
            Long healthCheckPollInterval) {
        super(
                blockchainService,
                blockSubscription,
                failureListener,
                subscriptionService,
                valueMonitor,
                eventStoreService,
                syncingThreshold,
                taskScheduler,
                healthCheckPollInterval);

        if (web3jService instanceof NaryoWebSocketService naryoWebSocketService) {
            this.webSocketClient = naryoWebSocketService.getWebSocketClient();
        } else {
            throw new BlockchainException(
                    "Non web socket service passed to WebSocketHealthCheckService");
        }
    }

    @Override
    protected boolean isSubscribed() {
        return super.isSubscribed() && webSocketClient.isOpen();
    }
}
