package io.naryo.infrastructure.broadcaster.rabbitmq.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.broadcaster.BroadcasterProducer;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterType;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.configuration.broadcaster.rabbitmq.Exchange;
import io.naryo.domain.configuration.broadcaster.rabbitmq.RabbitMqBroadcasterConfiguration;
import io.naryo.domain.configuration.broadcaster.rabbitmq.RoutingKey;
import io.naryo.domain.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static io.naryo.domain.RabbitMqConstants.RABBITMQ_TYPE;

@Slf4j
@Component
public final class RabbitMqBroadcasterProducer implements BroadcasterProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public RabbitMqBroadcasterProducer(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void produce(
            Broadcaster broadcaster, BroadcasterConfiguration configuration, Event event) {
        final Exchange exchange = ((RabbitMqBroadcasterConfiguration) configuration).getExchange();
        final RoutingKey routingKey =
                ((RabbitMqBroadcasterConfiguration) configuration).getRoutingKey();

        try {
            rabbitTemplate.convertAndSend(
                    exchange.value(), routingKey.value(), objectMapper.writeValueAsString(event));
        } catch (Exception e) {
            log.error("Error while sending event to RabbitMQ broadcaster: {}", e.getMessage());
        }
    }

    @Override
    public boolean supports(BroadcasterType type) {
        return type.getName().equals(RABBITMQ_TYPE);
    }
}
