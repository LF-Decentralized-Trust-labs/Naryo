package io.naryo.utils;

import java.io.IOException;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("rabbitmq")
public class RabbitConsumerFactory {

    private final ObjectMapper objectMapper;
    private final ConnectionFactory connectionFactory;

    public RabbitConsumerFactory(ObjectMapper objectMapper, ConnectionFactory connectionFactory) {
        this.objectMapper = objectMapper;
        this.connectionFactory = connectionFactory;
    }

    public <E> SimpleMessageListenerContainer createMessageListener(
            Class<E> instanceClass, Consumer<E> consumer, String... queues) {
        return createContainer(
                message -> {
                    try {
                        E event = objectMapper.readValue(message.getBody(), instanceClass);
                        consumer.accept(event);
                    } catch (IOException e) {
                        log.error("Failed to deserialize record: {}", message.getBody(), e);
                        throw new RuntimeException(e);
                    }
                },
                queues);
    }

    private SimpleMessageListenerContainer createContainer(
            MessageListener messageListener, String... queues) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(messageListener);
        container.setConcurrentConsumers(1);
        container.setQueueNames(queues);
        container.setAutoStartup(true);
        container.start();
        return container;
    }
}
