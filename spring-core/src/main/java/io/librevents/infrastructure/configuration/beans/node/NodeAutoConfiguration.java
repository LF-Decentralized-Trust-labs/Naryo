package io.librevents.infrastructure.configuration.beans.node;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.librevents.application.common.Mapper;
import io.librevents.application.event.decoder.ContractEventParameterDecoder;
import io.librevents.application.event.decoder.block.DefaultContractEventParameterDecoder;
import io.librevents.application.node.configuration.manager.DefaultNodeConfigurationManager;
import io.librevents.application.node.configuration.manager.NodeConfigurationManager;
import io.librevents.application.node.configuration.provider.NodeConfigurationProvider;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.interactor.block.factory.BlockInteractorFactory;
import io.librevents.application.node.interactor.block.factory.DefaultBlockInteractorFactory;
import io.librevents.application.node.interactor.block.factory.eth.EthereumRpcBlockInteractorFactory;
import io.librevents.application.node.interactor.block.factory.hedera.HederaMirrorNodeBlockInteractorFactory;
import io.librevents.application.node.interactor.block.mapper.BlockToBlockEventMapper;
import io.librevents.application.node.subscription.block.factory.BlockSubscriberFactory;
import io.librevents.application.node.subscription.block.factory.DefaultBlockSubscriberFactory;
import io.librevents.domain.event.block.BlockEvent;
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
    @ConditionalOnMissingBean(NodeConfigurationManager.class)
    public NodeConfigurationManager nodeConfigurationManager(
            List<NodeConfigurationProvider> providers) {
        return new DefaultNodeConfigurationManager(providers);
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
    @ConditionalOnMissingBean(BlockSubscriberFactory.class)
    public BlockSubscriberFactory blockSubscriberFactory(Mapper<Block, BlockEvent> blockMapper) {
        return new DefaultBlockSubscriberFactory(blockMapper);
    }

    @Bean
    @ConditionalOnMissingBean(ContractEventParameterDecoder.class)
    public ContractEventParameterDecoder contractEventParameterDecoder() {
        return new DefaultContractEventParameterDecoder();
    }
}
