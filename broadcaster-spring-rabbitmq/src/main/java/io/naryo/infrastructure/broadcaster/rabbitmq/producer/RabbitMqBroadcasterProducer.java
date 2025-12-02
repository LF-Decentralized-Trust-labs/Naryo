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
import io.naryo.domain.event.EventType;
import io.naryo.domain.event.contract.ContractEvent;
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
        broadcaster
                .getTarget()
                .getDestinations()
                .forEach(
                        destination -> {
                            RoutingKey routingKey = new RoutingKey(destination.value());
                            this.produce(exchange, routingKey, event);
                        });
    }

    private void produce(Exchange exchange, RoutingKey routingKey, Event<?> event) {
        try {
            String routingKeyValue =
                    event.getEventType() == EventType.CONTRACT
                            ? String.format(
                                    "%s.%s",
                                    routingKey.value(), getContractEventId((ContractEvent) event))
                            : String.format("%s.%s", routingKey.value(), event.getKey());
            rabbitTemplate.convertAndSend(
                    exchange.value(), routingKeyValue, objectMapper.writeValueAsString(event));
        } catch (Exception e) {
            log.error("Error while sending event to RabbitMQ broadcaster: {}", e.getMessage());
        }
    }

    @Override
    public boolean supports(BroadcasterType type) {
        return type.getName().equals(RABBITMQ_TYPE);
    }

    private String getContractEventId(ContractEvent event) {
        return String.format("%s.%s", event.getName().value(), event.getContractAddress());
    }
}
