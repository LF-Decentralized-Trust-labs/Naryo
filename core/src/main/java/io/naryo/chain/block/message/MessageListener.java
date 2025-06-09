package io.naryo.chain.block.message;

import io.naryo.dto.message.MessageDetails;

public interface MessageListener {

    void onMessage(MessageDetails message);
}
