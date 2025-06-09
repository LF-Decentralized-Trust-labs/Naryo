/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.naryo.chain.config;

import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.naryo.chain.config.factory.ContractEventDetailsFactoryFactoryBean;
import io.naryo.chain.service.BlockchainService;
import io.naryo.chain.service.HederaService;
import io.naryo.chain.service.container.NodeServices;
import io.naryo.chain.service.health.NodeHealthCheckService;
import io.naryo.chain.service.health.WebSocketHealthCheckService;
import io.naryo.chain.service.health.strategy.HttpReconnectionStrategy;
import io.naryo.chain.service.health.strategy.WebSocketResubscribeNodeFailureListener;
import io.naryo.chain.service.strategy.PollingBlockSubscriptionStrategy;
import io.naryo.chain.service.strategy.PubSubBlockSubscriptionStrategy;
import io.naryo.chain.settings.BlockStrategy;
import io.naryo.chain.settings.ChainType;
import io.naryo.chain.settings.Node;
import io.naryo.chain.settings.NodeSettings;
import jakarta.xml.bind.DatatypeConverter;
import lombok.AllArgsConstructor;
import okhttp3.ConnectionPool;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.NaryoWebSocketService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.utils.Async;

@AllArgsConstructor
public class NodeBeanRegistrationStrategy {

    private static final String WEB3J_SERVICE_BEAN_NAME = "%sWeb3jService";

    private static final String CONTRACT_EVENT_DETAILS_FACTORY_BEAN_NAME =
            "%sContractEventDetailsFactory";

    private static final String NODE_SERVICES_BEAN_NAME = "%sNodeServices";

    private static final String NODE_HEALTH_CHECK_BEAN_NAME = "%sNodeHealthCheck";

    private static final String NODE_FAILURE_LISTENER_BEAN_NAME = "%sNodeFailureListener";

    private static final String NODE_BLOCK_SUB_STRATEGY_BEAN_NAME = "%sBlockSubscriptionStategy";

    private static final String HEDERA_SERVICE_BEAN_NAME = "%sHederaService";

    private NodeSettings nodeSettings;
    private OkHttpClient globalOkHttpClient;

    public void register(Node node, BeanDefinitionRegistry registry) {
        String blockchainServiceBeanName = null;
        String blockSubStrategyBeanName = null;
        String hederaServiceBeanName = null;

        final Web3jService web3jService = buildWeb3jService(node);

        final Web3j web3j = buildWeb3j(node, web3jService);

        registerContractEventDetailsFactoryBean(node, registry);

        switch (node.getChainType()) {
            case ETHEREUM:
                blockchainServiceBeanName = registerBlockchainServiceBean(node, web3j, registry);

                blockSubStrategyBeanName =
                        registerBlockSubscriptionStrategyBean(node, web3j, registry, null);

                final String nodeFailureListenerBeanName =
                        registerNodeFailureListener(
                                node, blockSubStrategyBeanName, web3jService, registry);

                registerNodeHealthCheckBean(
                        node,
                        blockchainServiceBeanName,
                        blockSubStrategyBeanName,
                        web3jService,
                        nodeFailureListenerBeanName,
                        registry);
                break;
            case HASHGRAPH:
                hederaServiceBeanName = buildHederaService(registry, node);
                blockSubStrategyBeanName =
                        registerBlockSubscriptionStrategyBean(
                                node, web3j, registry, hederaServiceBeanName);
                break;
            default:
                break;
        }

        registerNodeServicesBean(
                node,
                web3j,
                blockchainServiceBeanName,
                blockSubStrategyBeanName,
                registry,
                hederaServiceBeanName);
    }

    // TODO register multiple nodes even when chains differ
    private void registerNodeServicesBean(
            Node node,
            Web3j web3j,
            String web3jServiceBeanName,
            String blockSubStrategyBeanName,
            BeanDefinitionRegistry registry,
            String hederaServiceBeanName) {
        final BeanDefinitionBuilder builder =
                BeanDefinitionBuilder.genericBeanDefinition(NodeServices.class);

        builder.addPropertyValue("nodeName", node.getName())
                .addPropertyValue("nodeType", node.getNodeType())
                .addPropertyValue("web3j", web3j)
                .addPropertyReference("blockSubscriptionStrategy", blockSubStrategyBeanName);

        if (node.getChainType().equals(ChainType.HASHGRAPH)) {
            builder.addPropertyReference("blockchainService", hederaServiceBeanName);
            builder.addPropertyReference("hederaService", hederaServiceBeanName);
        } else {
            builder.addPropertyReference("blockchainService", web3jServiceBeanName)
                    .addPropertyValue("hederaService", null);
        }

        final String beanName = String.format(NODE_SERVICES_BEAN_NAME, node.getName());
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    private void registerContractEventDetailsFactoryBean(
            Node node, BeanDefinitionRegistry registry) {
        final BeanDefinitionBuilder builder =
                BeanDefinitionBuilder.genericBeanDefinition(
                        ContractEventDetailsFactoryFactoryBean.class);

        builder.addPropertyReference("parameterConverter", "web3jEventParameterConverter")
                .addPropertyValue("node", node)
                .addPropertyValue("nodeName", node.getName());

        registry.registerBeanDefinition(
                String.format(CONTRACT_EVENT_DETAILS_FACTORY_BEAN_NAME, node.getName()),
                builder.getBeanDefinition());
    }

    private String registerBlockchainServiceBean(
            Node node, Web3j web3j, BeanDefinitionRegistry registry) {
        Class<? extends BlockchainService> blockchainService =
                io.naryo.chain.service.Web3jService.class;

        final BeanDefinitionBuilder builder =
                BeanDefinitionBuilder.genericBeanDefinition(blockchainService);

        builder.addConstructorArgValue(node.getName())
                .addConstructorArgValue(web3j)
                .addConstructorArgReference(
                        String.format(CONTRACT_EVENT_DETAILS_FACTORY_BEAN_NAME, node.getName()));

        final String beanName = String.format(WEB3J_SERVICE_BEAN_NAME, node.getName());
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());

        return beanName;
    }

    private void registerNodeHealthCheckBean(
            Node node,
            String blockchainServiceBeanName,
            String blockSubStrategyBeanName,
            Web3jService web3jService,
            String nodeFailureListenerBeanName,
            BeanDefinitionRegistry registry) {
        final BeanDefinitionBuilder builder;

        if (isWebSocketUrl(node.getUrl())) {
            builder =
                    BeanDefinitionBuilder.genericBeanDefinition(WebSocketHealthCheckService.class)
                            .addConstructorArgValue(web3jService);
        } else {
            builder = BeanDefinitionBuilder.genericBeanDefinition(NodeHealthCheckService.class);
        }

        builder.addConstructorArgReference(blockchainServiceBeanName);
        builder.addConstructorArgReference(blockSubStrategyBeanName);
        builder.addConstructorArgReference(nodeFailureListenerBeanName);
        builder.addConstructorArgReference("defaultSubscriptionService");
        builder.addConstructorArgReference("naryoValueMonitor");
        builder.addConstructorArgReference("defaultEventStoreService");
        builder.addConstructorArgValue(node.getSyncingThreshold());
        builder.addConstructorArgReference("scheduler");
        builder.addConstructorArgValue(node.getHealthcheckInterval());

        final String beanName = String.format(NODE_HEALTH_CHECK_BEAN_NAME, node.getName());
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    private String registerNodeFailureListener(
            Node node,
            String blockSubStrategyBeanName,
            Web3jService web3jService,
            BeanDefinitionRegistry registry) {
        final BeanDefinition beanDefinition;

        if (isWebSocketUrl(node.getUrl())) {
            final NaryoWebSocketService webSocketService = (NaryoWebSocketService) web3jService;
            beanDefinition =
                    BeanDefinitionBuilder.genericBeanDefinition(
                                    WebSocketResubscribeNodeFailureListener.class)
                            .getBeanDefinition();

            beanDefinition
                    .getConstructorArgumentValues()
                    .addIndexedArgumentValue(3, webSocketService.getWebSocketClient());

        } else {
            beanDefinition =
                    BeanDefinitionBuilder.genericBeanDefinition(HttpReconnectionStrategy.class)
                            .getBeanDefinition();
        }

        beanDefinition
                .getConstructorArgumentValues()
                .addIndexedArgumentValue(1, new RuntimeBeanReference(blockSubStrategyBeanName));

        final String beanName = String.format(NODE_FAILURE_LISTENER_BEAN_NAME, node.getName());
        registry.registerBeanDefinition(beanName, beanDefinition);

        return beanName;
    }

    private Web3jService buildWeb3jService(Node node) {
        Web3jService web3jService = null;

        Map<String, String> authHeaders;
        if (node.getUsername() != null && node.getPassword() != null) {
            authHeaders = new HashMap<>();
            authHeaders.put(
                    "Authorization",
                    "Basic "
                            + DatatypeConverter.printBase64Binary(
                                    String.format("%s:%s", node.getUsername(), node.getPassword())
                                            .getBytes()));
        } else {
            authHeaders = null;
        }

        if (isWebSocketUrl(node.getUrl())) {
            final URI uri = parseURI(node.getUrl());

            final WebSocketClient client =
                    authHeaders != null
                            ? new WebSocketClient(uri, authHeaders)
                            : new WebSocketClient(uri);

            WebSocketService wsService = new NaryoWebSocketService(client, false);

            try {
                wsService.connect();
            } catch (ConnectException e) {
                throw new RuntimeException("Unable to connect to eth node websocket", e);
            }

            web3jService = wsService;
        } else {

            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            ConnectionPool pool =
                    new ConnectionPool(
                            node.getMaxIdleConnections(),
                            node.getKeepAliveDuration(),
                            TimeUnit.MILLISECONDS);
            OkHttpClient client =
                    globalOkHttpClient
                            .newBuilder()
                            .connectionPool(pool)
                            .cookieJar(new JavaNetCookieJar(cookieManager))
                            .readTimeout(node.getReadTimeout(), TimeUnit.MILLISECONDS)
                            .connectTimeout(node.getConnectionTimeout(), TimeUnit.MILLISECONDS)
                            .build();
            HttpService httpService = new HttpService(node.getUrl(), client, false);
            if (authHeaders != null) {
                httpService.addHeaders(authHeaders);
            }
            web3jService = httpService;
        }

        return web3jService;
    }

    private Web3j buildWeb3j(Node node, Web3jService web3jService) {

        return Web3j.build(web3jService, node.getPollingInterval(), Async.defaultExecutorService());
    }

    private String buildHederaService(BeanDefinitionRegistry registry, Node node) {
        final BeanDefinitionBuilder builder =
                BeanDefinitionBuilder.genericBeanDefinition(HederaService.class);
        builder.addConstructorArgReference(
                String.format(CONTRACT_EVENT_DETAILS_FACTORY_BEAN_NAME, node.getName()));
        builder.addConstructorArgReference("defaultEventStoreService");
        builder.addConstructorArgReference("objectMapper");
        builder.addConstructorArgValue(node);
        builder.addConstructorArgValue(Executors.newScheduledThreadPool(10));
        builder.addConstructorArgReference("modelMapper");
        builder.addConstructorArgValue(globalOkHttpClient);
        final String beanName = String.format(HEDERA_SERVICE_BEAN_NAME, node.getName());
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());

        return beanName;
    }

    private String registerBlockSubscriptionStrategyBean(
            Node node, Web3j web3j, BeanDefinitionRegistry registry, String hederaServiceBeanName) {
        BlockStrategy nodeBlockStrategy = node.getBlockStrategy();
        BeanDefinitionBuilder builder = getBeanDefinitionBuilder(nodeBlockStrategy);

        builder.addConstructorArgValue(web3j)
                .addConstructorArgValue(node.getName())
                .addConstructorArgValue(node.getNodeType())
                .addConstructorArgReference("asyncTaskService")
                .addConstructorArgReference("defaultBlockNumberService");

        if (nodeBlockStrategy == BlockStrategy.POLL) {
            if (node.getChainType() == ChainType.HASHGRAPH) {
                builder.addConstructorArgReference(hederaServiceBeanName);
            }
            builder.addConstructorArgValue(node.getPollingInterval());
        }

        final String beanName = String.format(NODE_BLOCK_SUB_STRATEGY_BEAN_NAME, node.getName());
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());

        return beanName;
    }

    private BeanDefinitionBuilder getBeanDefinitionBuilder(BlockStrategy nodeBlockStrategy) {
        BlockStrategy blockStrategy =
                nodeBlockStrategy != null
                        ? nodeBlockStrategy
                        : BlockStrategy.valueOf(nodeSettings.getBlockStrategy());

        return switch (blockStrategy) {
            case POLL ->
                    BeanDefinitionBuilder.genericBeanDefinition(
                            PollingBlockSubscriptionStrategy.class);
            case PUBSUB ->
                    BeanDefinitionBuilder.genericBeanDefinition(
                            PubSubBlockSubscriptionStrategy.class);
        };
    }

    private boolean isWebSocketUrl(String nodeUrl) {
        return nodeUrl.contains("wss://") || nodeUrl.contains("ws://");
    }

    private URI parseURI(String serverUrl) {
        try {
            return new URI(serverUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(String.format("Failed to parse URL: '%s'", serverUrl), e);
        }
    }
}
