package io.naryo.infrastructure.configuration.beans.node;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.naryo.application.broadcaster.BroadcasterProducer;
import io.naryo.application.broadcaster.configuration.manager.BroadcasterConfigurationConfigurationManager;
import io.naryo.application.broadcaster.configuration.manager.BroadcasterConfigurationManager;
import io.naryo.application.common.Mapper;
import io.naryo.application.configuration.resilence.ResilienceRegistry;
import io.naryo.application.configuration.source.model.node.subscription.factory.BlockSubscriptionFactory;
import io.naryo.application.configuration.source.model.node.subscription.factory.DefaultBlockSubscriptionFactory;
import io.naryo.application.event.decoder.ContractEventParameterDecoder;
import io.naryo.application.event.decoder.block.DefaultContractEventParameterDecoder;
import io.naryo.application.filter.configuration.manager.FilterConfigurationManager;
import io.naryo.application.node.NodeConfigurationFacade;
import io.naryo.application.node.NodeInitializer;
import io.naryo.application.node.configuration.manager.DefaultNodeConfigurationManager;
import io.naryo.application.node.configuration.manager.NodeConfigurationManager;
import io.naryo.application.node.configuration.provider.NodeSourceProvider;
import io.naryo.application.node.interactor.block.dto.Block;
import io.naryo.application.node.interactor.block.factory.BlockInteractorFactory;
import io.naryo.application.node.interactor.block.factory.DefaultBlockInteractorFactory;
import io.naryo.application.node.interactor.block.factory.eth.EthereumRpcBlockInteractorFactory;
import io.naryo.application.node.interactor.block.factory.hedera.HederaMirrorNodeBlockInteractorFactory;
import io.naryo.application.node.interactor.block.mapper.BlockToBlockEventMapper;
import io.naryo.application.node.subscription.block.factory.BlockSubscriberFactory;
import io.naryo.application.node.subscription.block.factory.DefaultBlockSubscriberFactory;
import io.naryo.application.node.trigger.permanent.block.ProcessorTriggerFactory;
import io.naryo.application.store.Store;
import io.naryo.application.store.configuration.manager.StoreConfigurationManager;
import io.naryo.domain.event.block.BlockEvent;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NodeAutoConfiguration {

    @Bean
    public Mapper<Block, BlockEvent> mapper() {
        return new BlockToBlockEventMapper();
    }

    @Bean
    @ConditionalOnMissingBean(BlockSubscriptionFactory.class)
    public BlockSubscriptionFactory blockSubscriptionFactory() {
        return new DefaultBlockSubscriptionFactory();
    }

    @Bean
    @ConditionalOnMissingBean(NodeConfigurationManager.class)
    public NodeConfigurationManager nodeConfigurationManager(
            List<NodeSourceProvider> providers, BlockSubscriptionFactory blockSubscriptionFactory) {
        return new DefaultNodeConfigurationManager(providers, blockSubscriptionFactory);
    }

    @Bean
    @ConditionalOnMissingBean(BlockInteractorFactory.class)
    public BlockInteractorFactory blockInteractorFactory(
            OkHttpClient httpClient,
            ObjectMapper objectMapper,
            ScheduledExecutorService scheduledExecutorService) {
        return new DefaultBlockInteractorFactory(
                new EthereumRpcBlockInteractorFactory(httpClient),
                new HederaMirrorNodeBlockInteractorFactory(
                        httpClient, objectMapper, scheduledExecutorService));
    }

    @Bean
    @ConditionalOnMissingBean(ResilienceRegistry.class)
    public ResilienceRegistry resilienceRegistry() {
        return new ResilienceRegistry();
    }

    @Bean
    @ConditionalOnMissingBean(BlockSubscriberFactory.class)
    public BlockSubscriberFactory blockSubscriberFactory(
            Mapper<Block, BlockEvent> blockMapper, ResilienceRegistry registry) {
        return new DefaultBlockSubscriberFactory(blockMapper, registry);
    }

    @Bean
    @ConditionalOnMissingBean(ContractEventParameterDecoder.class)
    public ContractEventParameterDecoder contractEventParameterDecoder() {
        return new DefaultContractEventParameterDecoder();
    }

    @Bean
    @ConditionalOnMissingBean(ProcessorTriggerFactory.class)
    public ProcessorTriggerFactory processorTriggerFactory(ContractEventParameterDecoder decoder) {
        return new ProcessorTriggerFactory(decoder);
    }

    @Bean
    @ConditionalOnMissingBean(NodeConfigurationFacade.class)
    public NodeConfigurationFacade nodeConfigurationFacade(
            BroadcasterConfigurationConfigurationManager
                    broadcasterConfigurationConfigurationManager,
            BroadcasterConfigurationManager broadcasterConfigurationManager,
            FilterConfigurationManager filterConfigurationManager,
            NodeConfigurationManager nodeConfigurationManager,
            StoreConfigurationManager storeConfigurationManager) {
        return new NodeConfigurationFacade(
                broadcasterConfigurationConfigurationManager,
                broadcasterConfigurationManager,
                filterConfigurationManager,
                nodeConfigurationManager,
                storeConfigurationManager);
    }

    @Bean
    public NodeInitializer nodeInitializer(
            NodeConfigurationFacade config,
            ResilienceRegistry resilienceRegistry,
            BlockInteractorFactory interactorFactory,
            BlockSubscriberFactory subscriberFactory,
            ProcessorTriggerFactory processorFactory,
            ContractEventParameterDecoder decoder,
            List<BroadcasterProducer> producers,
            Set<Store<?, ?, ?>> stores) {
        return new NodeInitializer(
                config,
                resilienceRegistry,
                interactorFactory,
                subscriberFactory,
                processorFactory,
                decoder,
                producers,
                stores);
    }
}
