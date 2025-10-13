package io.naryo.infrastructure.configuration.beans.revision;

import org.springframework.beans.factory.InitializingBean;

public interface HookInitializer extends InitializingBean {

    void initialize();

    @Override
    default void afterPropertiesSet() {
        initialize();
    }
}
