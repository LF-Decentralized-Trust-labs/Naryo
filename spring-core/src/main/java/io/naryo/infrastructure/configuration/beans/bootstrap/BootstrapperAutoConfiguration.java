package io.naryo.infrastructure.configuration.beans.bootstrap;

import io.naryo.application.bootstrap.Bootstrapper;
import io.naryo.application.bootstrap.DefaultBootstrapper;
import io.naryo.application.configuration.revision.hook.RevisionHookBinder;
import io.naryo.application.configuration.revision.manager.ConfigurationRevisionManagers;
import io.naryo.application.node.NodeInitializer;
import io.naryo.application.node.NodeLifecycle;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BootstrapperAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Bootstrapper.class)
    public Bootstrapper bootstrapper(
            ConfigurationRevisionManagers managers,
            NodeInitializer nodeInitializer,
            NodeLifecycle nodeLifecycle,
            RevisionHookBinder hookBinder) {
        return new DefaultBootstrapper(managers, nodeInitializer, nodeLifecycle, hookBinder);
    }
}
