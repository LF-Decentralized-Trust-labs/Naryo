package io.naryo.api.node.common.interaction;

import io.naryo.api.RequestBuilder;
import io.naryo.api.node.common.request.interaction.InteractionConfigurationRequest;

public abstract class InteractionConfigurationRequestBuilder<
                T extends InteractionConfigurationRequestBuilder<T, Y>,
                Y extends InteractionConfigurationRequest>
        implements RequestBuilder<T, Y> {}
