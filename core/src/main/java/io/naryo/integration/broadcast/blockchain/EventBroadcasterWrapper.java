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

package io.naryo.integration.broadcast.blockchain;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.naryo.dto.block.BlockDetails;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.message.MessageDetails;
import io.naryo.dto.transaction.TransactionDetails;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * This broadcaster also ensures that the same message is only sent once (by storing sent events in
 * a short lives cache and not sending events if a cache match is found).
 *
 * <p>The cache expiration time can be configured with the broadcaster.cache.expirationMillis
 * property.
 *
 * @author Craig Williams craig.williams@consensys.net
 */
public class EventBroadcasterWrapper implements BlockchainEventBroadcaster {

    private Cache<Integer, ContractEventDetails> contractEventCache;

    private Cache<Integer, TransactionDetails> transactionCache;

    private Cache<Integer, MessageDetails> messageCache;

    private Long expirationTimeMillis;

    private BlockchainEventBroadcaster wrapped;

    private boolean enableBlockNotifications;

    public EventBroadcasterWrapper(
            Long expirationTimeMillis,
            BlockchainEventBroadcaster toWrap,
            boolean enableBlockNotifications) {
        this.expirationTimeMillis = expirationTimeMillis;
        this.contractEventCache = createCache();
        this.transactionCache = createCache();
        this.messageCache = createCache();
        this.wrapped = toWrap;
        this.enableBlockNotifications = enableBlockNotifications;
    }

    @Override
    public void broadcastNewBlock(BlockDetails block) {
        if (!this.enableBlockNotifications) {
            return;
        }

        wrapped.broadcastNewBlock(block);
    }

    @Override
    public void broadcastContractEvent(ContractEventDetails eventDetails) {
        synchronized (this) {
            if (contractEventCache.getIfPresent(Integer.valueOf(eventDetails.hashCode())) == null) {
                contractEventCache.put(Integer.valueOf(eventDetails.hashCode()), eventDetails);
                wrapped.broadcastContractEvent(eventDetails);
            }
        }
    }

    @Override
    public void broadcastTransaction(TransactionDetails transactionDetails) {
        synchronized (this) {
            if (transactionCache.getIfPresent(Integer.valueOf(transactionDetails.hashCode()))
                    == null) {
                transactionCache.put(
                        Integer.valueOf(transactionDetails.hashCode()), transactionDetails);
                wrapped.broadcastTransaction(transactionDetails);
            }
        }
    }

    @Override
    public void broadcastMessage(MessageDetails messageDetails) {
        synchronized (this) {
            if (messageCache.getIfPresent(Integer.valueOf(messageDetails.hashCode())) == null) {
                messageCache.put(Integer.valueOf(messageDetails.hashCode()), messageDetails);
                wrapped.broadcastMessage(messageDetails);
            }
        }
    }

    @Scheduled(fixedRateString = "${broadcaster.cache.expirationMillis}")
    public void cleanUpCache() {
        contractEventCache.cleanUp();
        transactionCache.cleanUp();
    }

    protected <T> Cache<Integer, T> createCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(expirationTimeMillis, TimeUnit.MILLISECONDS)
                .build();
    }
}
