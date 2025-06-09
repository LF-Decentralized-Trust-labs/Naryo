package io.naryo.tests;

import io.naryo.utils.RabbitConsumerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = {"mongo", "rabbitmq"})
public class RabbitBroadcasterSingleNodeTest extends BaseRabbitBroadcasterTest {

    @Autowired
    public RabbitBroadcasterSingleNodeTest(
            RabbitConsumerFactory consumerFactory, RabbitAdmin admin) {
        super(consumerFactory, admin);
    }
}
