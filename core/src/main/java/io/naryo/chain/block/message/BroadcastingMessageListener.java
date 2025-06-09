package io.naryo.chain.block.message;

import io.naryo.dto.message.MessageDetails;
import io.naryo.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import io.naryo.integration.eventstore.EventStore;
import io.naryo.integration.eventstore.SaveableEventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BroadcastingMessageListener implements MessageListener {

    private final EventStore eventStore;
    private final BlockchainEventBroadcaster eventBroadcaster;

    @Autowired
    public BroadcastingMessageListener(
            EventStore eventStore, BlockchainEventBroadcaster eventBroadcaster) {
        this.eventStore = eventStore;
        this.eventBroadcaster = eventBroadcaster;
    }

    @Override
    public void onMessage(MessageDetails messageDetails) {
        eventBroadcaster.broadcastMessage(messageDetails);
        if (eventStore instanceof SaveableEventStore saveableEventStore) {
            saveableEventStore.save(messageDetails);
        }
    }
}
