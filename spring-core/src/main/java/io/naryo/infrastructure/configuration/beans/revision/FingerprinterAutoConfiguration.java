package io.naryo.infrastructure.configuration.beans.revision;

import io.naryo.application.broadcaster.configuration.revision.BroadcasterConfigurationRevisionFingerprinter;
import io.naryo.application.broadcaster.revision.BroadcasterRevisionFingerprinter;
import io.naryo.application.filter.revision.FilterRevisionFingerprinter;
import io.naryo.application.node.revision.NodeRevisionFingerprinter;
import io.naryo.application.store.revision.StoreRevisionFingerprinter;
import io.naryo.domain.configuration.broadcaster.BroadcasterConfigurationNormalizer;
import io.naryo.domain.configuration.store.StoreConfigurationNormalizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FingerprinterAutoConfiguration {

    @Bean
    public NodeRevisionFingerprinter nodeRevisionFingerprinter() {
        return new NodeRevisionFingerprinter();
    }

    @Bean
    public BroadcasterRevisionFingerprinter broadcasterRevisionFingerprinter() {
        return new BroadcasterRevisionFingerprinter();
    }

    @Bean
    public FilterRevisionFingerprinter filterRevisionFingerprinter() {
        return new FilterRevisionFingerprinter();
    }

    @Bean
    public BroadcasterConfigurationRevisionFingerprinter
            broadcasterConfigurationRevisionFingerprinter(
                    BroadcasterConfigurationNormalizer normalizer) {
        return new BroadcasterConfigurationRevisionFingerprinter(normalizer);
    }

    @Bean
    public StoreRevisionFingerprinter storeRevisionFingerprinter(
            StoreConfigurationNormalizer normalizer) {
        return new StoreRevisionFingerprinter(normalizer);
    }
}
