package io.naryo.application.node.interactor.block.factory.eth;

import java.net.*;
import java.util.concurrent.TimeUnit;

import io.naryo.domain.node.Node;
import io.naryo.domain.node.connection.NodeConnection;
import io.naryo.domain.node.connection.http.HttpNodeConnection;
import io.naryo.domain.node.connection.ws.WsNodeConnection;
import io.naryo.domain.node.ethereum.priv.PrivateEthereumNode;
import io.naryo.infrastructure.node.interactor.eth.rpc.EthereumRpcBlockInteractor;
import io.naryo.infrastructure.node.interactor.eth.rpc.PrivateEthereumRpcBlockInteractor;
import okhttp3.ConnectionPool;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.besu.Besu;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

public final class EthereumRpcBlockInteractorFactory {

    private final OkHttpClient httpClient;

    public EthereumRpcBlockInteractorFactory(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public EthereumRpcBlockInteractor create(Node node) {
        Web3jService web3jService = getWeb3jService(node.getConnection());
        Web3j web3j = Web3j.build(web3jService);
        Besu besu = Besu.build(web3jService);

        if (node instanceof PrivateEthereumNode) {
            return new PrivateEthereumRpcBlockInteractor(web3j, besu);
        }
        return new EthereumRpcBlockInteractor(web3j);
    }

    private Web3jService getWeb3jService(NodeConnection connection) {
        return switch (connection.getType()) {
            case HTTP -> createHttpService((HttpNodeConnection) connection);
            case WS -> createWebSocketService((WsNodeConnection) connection);
        };
    }

    private HttpService createHttpService(HttpNodeConnection connection) {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        ConnectionPool pool =
                new ConnectionPool(
                        connection.getMaxIdleConnections().value(),
                        connection.getKeepAliveDuration().value().getSeconds(),
                        TimeUnit.SECONDS);
        OkHttpClient client =
                httpClient
                        .newBuilder()
                        .connectionPool(pool)
                        .cookieJar(new JavaNetCookieJar(cookieManager))
                        .readTimeout(connection.getReadTimeout().value())
                        .connectTimeout(connection.getConnectionTimeout().value())
                        .build();
        return new HttpService(connection.getEndpoint().getUrl(), client, false);
    }

    private WebSocketService createWebSocketService(WsNodeConnection connection) {
        final URI uri = parseURI(connection.getEndpoint().getUrl());

        final WebSocketClient client =
                connection.getEndpoint().getHeaders() != null
                        ? new WebSocketClient(uri, connection.getEndpoint().getHeaders())
                        : new WebSocketClient(uri);

        WebSocketService wsService = new WebSocketService(client, false);

        try {
            wsService.connect();
        } catch (ConnectException e) {
            throw new RuntimeException("Unable to connect to eth node websocket", e);
        }

        return wsService;
    }

    private URI parseURI(String serverUrl) {
        try {
            return new URI(serverUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(String.format("Failed to parse URL: '%s'", serverUrl), e);
        }
    }
}
