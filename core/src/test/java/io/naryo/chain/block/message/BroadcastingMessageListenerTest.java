package io.naryo.chain.block.message;

import java.time.Instant;

import io.naryo.dto.message.MessageDetails;
import io.naryo.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import io.naryo.integration.eventstore.SaveableEventStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BroadcastingMessageListenerTest {

    @Mock private SaveableEventStore saveableEventStore;

    @Mock private BlockchainEventBroadcaster eventBroadcaster;

    private BroadcastingMessageListener underTest;

    private static final MessageDetails messageDetails;

    static {
        messageDetails = new MessageDetails();
        messageDetails.setMessage("Test");
        messageDetails.setTopicId("0.0.1");
        messageDetails.setNodeName("default");
        messageDetails.setSequenceNumber(Long.valueOf("0"));
        messageDetails.setTimestamp(Instant.now().getEpochSecond());
    }

    @BeforeEach
    public void init() {
        doNothing().when(saveableEventStore).save(messageDetails);
        doNothing().when(eventBroadcaster).broadcastMessage(messageDetails);
        underTest = new BroadcastingMessageListener(saveableEventStore, eventBroadcaster);
    }

    @Test
    void testReceiveMessage() {
        this.underTest.onMessage(messageDetails);

        verify(eventBroadcaster, times(1)).broadcastMessage(messageDetails);
        verify(saveableEventStore, times(1)).save(messageDetails);
    }
}
