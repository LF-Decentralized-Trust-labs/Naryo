package io.naryo.infrastructure.broadcaster;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.broadcaster.BroadcasterProducer;
import io.naryo.domain.broadcaster.Broadcaster;
import io.naryo.domain.broadcaster.BroadcasterType;
import io.naryo.domain.common.Destination;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfiguration;
import io.naryo.domain.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static io.naryo.domain.KafkaConstants.KAFKA_TYPE;

@Slf4j
@Component
public final class KafkaBroadcasterProducer implements BroadcasterProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaBroadcasterProducer(
            KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void produce(
            Broadcaster broadcaster, BroadcasterConfiguration configuration, Event<?> event) {
        for (Destination destination : broadcaster.getTarget().getDestinations()) {
            try {
                kafkaTemplate.send(destination.value(), objectMapper.writeValueAsString(event));
            } catch (Exception e) {
                log.error("Error while sending event to Kafka broadcaster: {}", e.getMessage());
            }
        }
    }

    @Override
    public boolean supports(BroadcasterType type) {
        return type.getName().equals(KAFKA_TYPE);
    }
}
