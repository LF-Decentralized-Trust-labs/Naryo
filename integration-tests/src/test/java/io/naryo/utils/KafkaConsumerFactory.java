package io.naryo.utils;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.dto.TransactionBasedDetails;
import io.naryo.dto.event.ContractEventDetails;
import io.naryo.dto.event.ContractEventStatus;
import io.naryo.dto.message.NaryoMessage;
import io.naryo.dto.transaction.TransactionDetails;
import io.naryo.dto.transaction.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.shaded.org.awaitility.Durations;

@Slf4j
@Component
@Profile("kafka")
public class KafkaConsumerFactory {

    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaConsumerFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static KafkaConsumer<String, String> createKafkaConsumer(
            String boostrapServer, String... topics) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServer);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG, String.format("test-group-%s", UUID.randomUUID()));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topics));
        return consumer;
    }

    public <T extends TransactionBasedDetails, V extends NaryoMessage<T>>
            Optional<V> getTransactionalMessage(
                    KafkaConsumer<String, String> consumer,
                    Class<V> instanceClass,
                    String topic,
                    Duration timeout,
                    String transactionHash) {
        Predicate<V> predicate = v -> v.getDetails().getTransactionHash().equals(transactionHash);
        return getMessage(consumer, instanceClass, topic, timeout, predicate);
    }

    public <T extends ContractEventDetails, V extends NaryoMessage<T>>
            Optional<V> getContractEventMessage(
                    KafkaConsumer<String, String> consumer,
                    Class<V> instanceClass,
                    String topic,
                    Duration timeout,
                    String transactionHash,
                    ContractEventStatus status) {
        Predicate<V> predicate =
                v ->
                        v.getDetails().getTransactionHash().equals(transactionHash)
                                && v.getDetails().getStatus().equals(status);
        return getMessage(consumer, instanceClass, topic, timeout, predicate);
    }

    public <T extends TransactionDetails, V extends NaryoMessage<T>>
            Optional<V> getTransactionMessage(
                    KafkaConsumer<String, String> consumer,
                    Class<V> instanceClass,
                    String topic,
                    Duration timeout,
                    String transactionHash,
                    TransactionStatus status) {
        Predicate<V> predicate =
                v ->
                        v.getDetails().getTransactionHash().equals(transactionHash)
                                && v.getDetails().getStatus().equals(status);
        return getMessage(consumer, instanceClass, topic, timeout, predicate);
    }

    public <T, V extends NaryoMessage<T>> List<V> getMessages(
            KafkaConsumer<String, String> consumer,
            Class<V> instanceClass,
            String topic,
            Duration timeout,
            Predicate<List<V>> predicate) {
        List<V> values = new ArrayList<>();
        Awaitility.await()
                .atMost(timeout)
                .pollInterval(Durations.ONE_SECOND)
                .until(
                        () -> {
                            values.addAll(pollConsumerRecord(consumer, instanceClass, topic));
                            return predicate.test(values);
                        });
        return values;
    }

    public <T, V extends NaryoMessage<T>> Optional<V> getMessage(
            KafkaConsumer<String, String> consumer,
            Class<V> instanceClass,
            String topic,
            Duration timeout,
            Predicate<V> predicate) {
        List<V> values = new ArrayList<>();
        Awaitility.await()
                .atMost(timeout)
                .pollInterval(Durations.ONE_SECOND)
                .until(
                        () -> {
                            values.addAll(pollConsumerRecord(consumer, instanceClass, topic));
                            return values.stream().anyMatch(predicate);
                        });
        return values.stream().filter(predicate).findFirst();
    }

    public <T, V extends NaryoMessage<T>> List<V> pollConsumerRecord(
            KafkaConsumer<String, String> consumer, Class<V> instanceClass, String topic) {
        final List<V> values = new ArrayList<>();
        ConsumerRecords<String, String> records = consumer.poll(Durations.ONE_SECOND);
        records.records(topic)
                .forEach(
                        consumerRecord -> {
                            try {
                                values.add(
                                        objectMapper.readValue(
                                                consumerRecord.value(), instanceClass));
                            } catch (JsonProcessingException e) {
                                log.error(
                                        "Failed to deserialize record: {}",
                                        consumerRecord.value(),
                                        e);
                                throw new RuntimeException(e);
                            }
                        });
        return values;
    }
}
