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

package io.naryo.chain.service.health.strategy;

import java.util.Objects;

import io.naryo.chain.service.strategy.BlockSubscriptionStrategy;
import io.naryo.chain.websocket.WebSocketReconnectionManager;
import io.naryo.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.websocket.WebSocketClient;

/**
 * An NodeFailureListener that reconnects to the websocket server on failure, and reconnects the
 * block subscription and resubscribes to all active event subscriptions on recovery.
 *
 * <p>Note: All subscriptions are unregistered before being registered.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
@Slf4j
public class WebSocketResubscribeNodeFailureListener extends ResubscribingReconnectionStrategy {

    private final WebSocketReconnectionManager reconnectionManager;
    private final WebSocketClient client;

    public WebSocketResubscribeNodeFailureListener(
            SubscriptionService subscriptionService,
            BlockSubscriptionStrategy blockSubscription,
            WebSocketReconnectionManager reconnectionManager,
            WebSocketClient client) {
        super(subscriptionService, blockSubscription);

        this.reconnectionManager = reconnectionManager;
        this.client = client;
    }

    @Override
    public void reconnect() {
        log.info(
                "Reconnecting web socket because of {} node failure",
                getBlockSubscriptionStrategy().getNodeName());
        reconnectionManager.reconnect(client);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WebSocketResubscribeNodeFailureListener that = (WebSocketResubscribeNodeFailureListener) o;
        return Objects.equals(reconnectionManager, that.reconnectionManager)
                && Objects.equals(client, that.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), reconnectionManager, client);
    }
}
