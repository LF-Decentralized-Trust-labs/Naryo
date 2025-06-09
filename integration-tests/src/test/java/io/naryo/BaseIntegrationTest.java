package io.naryo;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import io.github.ganchix.ganache.GanacheContainer;
import io.github.ganchix.ganache.LogGanacheExtractorConsumer;
import io.naryo.container.MyGanacheContainer;
import io.naryo.contract.EventEmitter;
import io.naryo.repository.ContractEventFilterRepository;
import io.naryo.repository.TransactionMonitoringSpecRepository;
import io.naryo.utils.EventFilterCreator;
import io.naryo.utils.KafkaConsumerFactory;
import io.naryo.utils.TransactionMonitorCreator;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.*;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Numeric;

import static io.naryo.container.MyGanacheContainer.GANACHE_CONTAINER_PORT;

@Slf4j
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestApplication.class)
@ContextConfiguration(initializers = BaseIntegrationTest.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class BaseIntegrationTest {

    public static final Path GANACHE_DB_PATH = Paths.get("./tmp/ganache_data");
    protected static final PortBinding GANACHE_PORT_BINDING;
    protected static final PortBinding KAFKA_PORT_BINDING;
    protected static final String ZERO_ADDRESS = "0x0000000000000000000000000000000000000000";
    protected static final BigInteger GAS_PRICE = BigInteger.ZERO;
    protected static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
    protected static final Credentials CREDENTIALS =
            Credentials.create(
                    "0x4d5db4107d237df6a3d58ee5f70ae63d73d7658d4026f2eefd2f204c81682cb7");
    protected static final MongoDBContainer mongoContainer = new MongoDBContainer("mongo:4.0.10");
    protected static final RabbitMQContainer rabbitContainer =
            new RabbitMQContainer("rabbitmq:3.7.17-management");
    protected static final KafkaContainer kafkaContainer =
            new KafkaContainer("apache/kafka-native:3.8.0");
    protected static final PostgreSQLContainer postgresContainer =
            new PostgreSQLContainer<>("postgres:16-alpine");
    protected static final GanacheContainer ganacheContainer;
    protected static Web3j web3j;
    protected static TransactionManager transactionManager;
    protected static ContractGasProvider contractGasProvider;
    protected static EventEmitter defaultContract;

    @Autowired protected EventFilterCreator eventFilterCreator;
    @Autowired protected ContractEventFilterRepository contractFilterRepository;
    @Autowired protected TransactionMonitoringSpecRepository transactionFilterRepository;
    @Autowired protected TransactionMonitorCreator transactionMonitorCreator;

    @Autowired(required = false)
    protected KafkaConsumerFactory kafkaConsumerFactory;

    static {
        ganacheContainer =
                new MyGanacheContainer(CREDENTIALS)
                        .withFileSystemBind(
                                GANACHE_DB_PATH.toFile().getAbsolutePath(),
                                "/ganache-db",
                                BindMode.READ_WRITE);
        ganacheContainer.withLogConsumer(new LogGanacheExtractorConsumer(log, ganacheContainer));

        Instant start = Instant.now();
        List.of(
                        mongoContainer,
                        rabbitContainer,
                        kafkaContainer,
                        postgresContainer,
                        ganacheContainer)
                .parallelStream()
                .forEach(GenericContainer::start);

        log.info(
                "\uD83D\uDC18 Postgres container started on port: {}",
                postgresContainer.getFirstMappedPort());
        log.info(
                "\uD83E\uDEB4 MongoDB container started on port: {}",
                mongoContainer.getFirstMappedPort());
        log.info(
                "\uD83E\uDD8A RabbitMQ container started on port: {}",
                rabbitContainer.getFirstMappedPort());
        log.info(
                "\uD83D\uDCED Kafka container started on port: {}",
                kafkaContainer.getFirstMappedPort());
        log.info(
                "\uD83C\uDF6B Ganache container started on port: {}",
                ganacheContainer.getFirstMappedPort());
        log.info(
                "\uD83D\uDC33 TestContainers started in {}",
                Duration.between(start, Instant.now()));

        web3j = ganacheContainer.getWeb3j();
        transactionManager = new RawTransactionManager(web3j, CREDENTIALS);
        contractGasProvider = new StaticGasProvider(GAS_PRICE, GAS_LIMIT);
        GANACHE_PORT_BINDING =
                new PortBinding(
                        new Ports.Binding(
                                ganacheContainer.getHost(),
                                String.valueOf(ganacheContainer.getFirstMappedPort())),
                        new ExposedPort(GANACHE_CONTAINER_PORT));
        KAFKA_PORT_BINDING =
                new PortBinding(
                        new Ports.Binding(
                                kafkaContainer.getHost(),
                                String.valueOf(kafkaContainer.getFirstMappedPort())),
                        new ExposedPort(9092));
    }

    @AfterEach
    void cleanUp() {
        contractFilterRepository.deleteAll();
        log.warn("\uD83E\uDDF9 Contract event filters deleted");
        transactionFilterRepository.deleteAll();
        log.warn("\uD83E\uDDF9 Transaction monitoring specs deleted");
    }

    @BeforeEach
    protected void setUp() {
        defaultContract = deployContract();
        log.info("\uD83D\uDC77 Default contract re-deployed");
    }

    @PreDestroy
    void cleanGanacheData() throws IOException {
        if (Files.exists(GANACHE_DB_PATH)) {
            Files.walk(GANACHE_DB_PATH)
                    .sorted((o1, o2) -> -o1.compareTo(o2))
                    .map(Path::toFile)
                    .forEach(File::delete);
            System.out.println("📌 Ganache data folder deleted when Spring context shutdown.");
        }
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                            "MONGO_HOST=" + mongoContainer.getHost(),
                            "MONGO_PORT=" + mongoContainer.getFirstMappedPort(),
                            "RABBIT_HOST=" + rabbitContainer.getHost(),
                            "RABBIT_PORT=" + rabbitContainer.getFirstMappedPort(),
                            "KAFKA_HOST=" + kafkaContainer.getHost(),
                            "KAFKA_PORT=" + kafkaContainer.getFirstMappedPort(),
                            "POSTGRES_HOST=" + postgresContainer.getHost(),
                            "POSTGRES_PORT=" + postgresContainer.getFirstMappedPort(),
                            "POSTGRES_USER=" + postgresContainer.getUsername(),
                            "POSTGRES_PASSWORD=" + postgresContainer.getPassword(),
                            "POSTGRES_DB=" + postgresContainer.getDatabaseName(),
                            "NODE_HOST=" + ganacheContainer.getHost(),
                            "NODE_PORT=" + ganacheContainer.getFirstMappedPort())
                    .applyTo(applicationContext);
        }
    }

    protected static EventEmitter deployContract() {
        try {
            return EventEmitter.deploy(web3j, transactionManager, contractGasProvider).send();
        } catch (Exception e) {
            log.error("Failed to deploy contract: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    protected static EventEmitter loadContract(String address) {
        try {
            return EventEmitter.load(address, web3j, transactionManager, contractGasProvider);
        } catch (Exception e) {
            log.error("Failed to load contract: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    protected void mineBlocks(Integer numberOfBlocks)
            throws ExecutionException, InterruptedException, IOException {
        for (int i = 0; i < numberOfBlocks; i++) {
            sendTransaction();
        }
    }

    protected BigInteger getNonce() throws ExecutionException, InterruptedException {
        EthGetTransactionCount ethGetTransactionCount =
                web3j.ethGetTransactionCount(
                                CREDENTIALS.getAddress(), DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get();
        return ethGetTransactionCount.getTransactionCount();
    }

    protected void sendTransaction() throws ExecutionException, InterruptedException, IOException {
        EthAccounts ethAccounts = web3j.ethAccounts().send();

        final Transaction tx =
                Transaction.createEtherTransaction(
                        ethAccounts.getAccounts().getFirst(),
                        getNonce(),
                        GAS_PRICE,
                        GAS_LIMIT,
                        ethAccounts.getAccounts().getLast(),
                        BigInteger.ONE);

        web3j.ethSendTransaction(tx).send();
    }

    protected String createRawSignedTransaction(String destination)
            throws ExecutionException, InterruptedException {
        final RawTransaction rawTransaction =
                RawTransaction.createEtherTransaction(
                        getNonce(), GAS_PRICE, GAS_LIMIT, destination, BigInteger.ONE);

        final byte[] signedTx = TransactionEncoder.signMessage(rawTransaction, CREDENTIALS);

        return Numeric.toHexString(signedTx);
    }

    protected String sendRawTransaction(String signedTxHex)
            throws ExecutionException, InterruptedException {
        final EthSendTransaction ethSendTransaction =
                web3j.ethSendRawTransaction(signedTxHex).sendAsync().get();
        return ethSendTransaction.getTransactionHash();
    }
}
