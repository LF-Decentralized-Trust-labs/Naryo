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

package io.naryo.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/** Mostly taken from org.springframework.boot.autoconfigure.condition.OnExpressionCondition */
@Order(Ordered.LOWEST_PRECEDENCE - 20)
class OnWebsocketCondition extends OnMultiExpressionCondition {

    private static final String WEBSOCKET_ENABLED_EXPRESSION =
            "'${ethereum.node.url}'.contains('wss://') || '${ethereum.node.url}'.contains('ws://')";

    private static final String WEBSOCKET_NOT_ENABLED_EXPRESSION =
            "!('${ethereum.node.url}'.contains('wss://') || '${ethereum.node.url}'.contains('ws://'))";

    public OnWebsocketCondition() {
        super(
                WEBSOCKET_ENABLED_EXPRESSION,
                WEBSOCKET_NOT_ENABLED_EXPRESSION,
                ConditionalOnWebsocket.class);
    }
}
