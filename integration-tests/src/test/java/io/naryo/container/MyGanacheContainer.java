package io.naryo.container;

import java.math.BigInteger;
import java.util.List;

import io.github.ganchix.ganache.GanacheContainer;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.wait.strategy.Wait;
import org.web3j.crypto.Credentials;

@Slf4j
public class MyGanacheContainer extends GanacheContainer<MyGanacheContainer> {

    public static final int GANACHE_CONTAINER_PORT = 8545;
    public static final BigInteger GAS_PRICE = BigInteger.ZERO;
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
    public static final BigInteger DEFAULT_BALANCE = BigInteger.valueOf(1_000_000_000_000_000L);

    protected final Credentials credentials;

    public MyGanacheContainer(Credentials credentials) {
        super();
        this.credentials = credentials;
    }

    @Override
    protected void configure() {
        withExposedPorts(8545);
        withExposedPorts(GANACHE_CONTAINER_PORT);
        waitingFor(Wait.forLogMessage(".*Listening on.*", 1));
        List<String> options =
                List.of(
                        "--gasLimit ".concat(GAS_LIMIT.toString()),
                        "--gasPrice ".concat(GAS_PRICE.toString()),
                        "--account "
                                .concat(
                                        generatePrivateKey(
                                                credentials
                                                        .getEcKeyPair()
                                                        .getPrivateKey()
                                                        .toString(16)))
                                .concat(",")
                                .concat(DEFAULT_BALANCE.toString()),
                        "--db /ganache-db");
        withCommand(String.join(" ", options));
    }

    private String generatePrivateKey(String privateKey) {
        return privateKey.startsWith("0x") ? privateKey : "0x".concat(privateKey);
    }
}
