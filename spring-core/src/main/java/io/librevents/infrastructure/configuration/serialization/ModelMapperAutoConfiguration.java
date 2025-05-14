package io.librevents.infrastructure.configuration.serialization;

import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.librevents.infrastructure.configuration.MainProperties;
import io.librevents.infrastructure.configuration.MainPropertiesSerializer;
import io.librevents.infrastructure.configuration.broadcaster.BroadcasterTypeRegistry;
import io.librevents.infrastructure.configuration.broadcaster.BroadcastingProperties;
import io.librevents.infrastructure.configuration.broadcaster.BroadcastingPropertiesDeserializer;
import io.librevents.infrastructure.configuration.broadcaster.configuration.BroadcasterConfigurationEntryProperties;
import io.librevents.infrastructure.configuration.broadcaster.configuration.BroadcasterConfigurationEntryPropertiesDeserializer;
import io.librevents.infrastructure.configuration.broadcaster.target.BroadcasterTargetEntryProperties;
import io.librevents.infrastructure.configuration.broadcaster.target.BroadcasterTargetEntryPropertiesDeserializer;
import io.librevents.infrastructure.configuration.node.NodeProperties;
import io.librevents.infrastructure.configuration.node.NodePropertiesDeserializer;
import io.librevents.infrastructure.configuration.node.connection.ConnectionProperties;
import io.librevents.infrastructure.configuration.node.connection.ConnectionPropertiesDeserializer;
import io.librevents.infrastructure.configuration.node.interaction.InteractionProperties;
import io.librevents.infrastructure.configuration.node.interaction.InteractionPropertiesDeserializer;
import io.librevents.infrastructure.configuration.node.interaction.block.BlockInteractionConfigurationProperties;
import io.librevents.infrastructure.configuration.node.interaction.block.BlockInteractionConfigurationPropertiesDeserializer;
import io.librevents.infrastructure.configuration.node.subscription.SubscriptionProperties;
import io.librevents.infrastructure.configuration.node.subscription.SubscriptionPropertiesDeserializer;
import io.librevents.infrastructure.configuration.node.subscription.block.BlockSubscriptionConfigurationProperties;
import io.librevents.infrastructure.configuration.node.subscription.block.BlockSubscriptionConfigurationPropertiesDeserializer;
import io.librevents.infrastructure.configuration.node.subscription.block.method.BlockSubscriptionMethodProperties;
import io.librevents.infrastructure.configuration.node.subscription.block.method.BlockSubscriptionMethodPropertiesDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper(BroadcasterTypeRegistry registry) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Duration.class, new DurationDeserializer());
        module.addDeserializer(MainProperties.class, new MainPropertiesSerializer());
        module.addDeserializer(
                BroadcastingProperties.class, new BroadcastingPropertiesDeserializer());
        module.addDeserializer(
                BroadcasterConfigurationEntryProperties.class,
                new BroadcasterConfigurationEntryPropertiesDeserializer(registry));
        module.addDeserializer(
                BroadcasterTargetEntryProperties.class,
                new BroadcasterTargetEntryPropertiesDeserializer());
        module.addDeserializer(NodeProperties.class, new NodePropertiesDeserializer());
        module.addDeserializer(
                SubscriptionProperties.class, new SubscriptionPropertiesDeserializer());
        module.addDeserializer(
                BlockSubscriptionConfigurationProperties.class,
                new BlockSubscriptionConfigurationPropertiesDeserializer());
        module.addDeserializer(
                InteractionProperties.class, new InteractionPropertiesDeserializer());
        module.addDeserializer(
                BlockSubscriptionMethodProperties.class,
                new BlockSubscriptionMethodPropertiesDeserializer());
        module.addDeserializer(
                BlockInteractionConfigurationProperties.class,
                new BlockInteractionConfigurationPropertiesDeserializer());
        module.addDeserializer(ConnectionProperties.class, new ConnectionPropertiesDeserializer());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        mapper.findAndRegisterModules();
        mapper.registerModule(module);
        return mapper;
    }
}
