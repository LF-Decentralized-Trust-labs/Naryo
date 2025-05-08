package io.librevents.infrastructure.node.interactor.hedera;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.interactor.block.dto.Log;
import io.librevents.application.node.interactor.block.dto.Transaction;
import io.librevents.infrastructure.node.interactor.hedera.exception.EmptyResponseException;
import io.librevents.infrastructure.node.interactor.hedera.exception.UnexpectedResponseException;
import io.librevents.infrastructure.node.interactor.hedera.http.MirrorNodeHttpClient;
import io.librevents.infrastructure.node.interactor.hedera.map.BlockConverter;
import io.librevents.infrastructure.node.interactor.hedera.map.LogConverter;
import io.librevents.infrastructure.node.interactor.hedera.map.TransactionConverter;
import io.librevents.infrastructure.node.interactor.hedera.response.*;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

@Slf4j
public final class HederaMirrorNodeBlockInteractor implements BlockInteractor {
    private static final String API_VERSION = "v1";
    private static final String BASE_PATH = "/api/" + API_VERSION;
    private static final String BLOCKS = "/blocks";
    private static final String CONTRACT_RESULTS = "/contracts/results";
    private static final String LOGS = "/contracts/results/logs";
    private static final String LOGS_FOR_CONTRACT = "/contracts/{contract}/results/logs";
    private static final String QUERY_LIMIT = "limit";
    private static final String QUERY_ORDER = "order";
    private static final String QUERY_TIMESTAMP = "timestamp";
    private static final String QUERY_BLOCK_NUMBER = "block.number";
    private static final String TOPIC0 = "topic0";
    private static final String LINK_NEXT = "next";

    private final MirrorNodeHttpClient client;
    private final Duration pollingInterval;
    private final int maxRetries;
    private final int limitPerPage;
    private final ScheduledExecutorService scheduler;

    public HederaMirrorNodeBlockInteractor(
            OkHttpClient httpClient,
            ObjectMapper objectMapper,
            String host,
            Map<String, String> headers,
            Duration pollingInterval,
            int maxRetries,
            int limitPerPage,
            ScheduledExecutorService scheduler) {
        Objects.requireNonNull(httpClient, "httpClient");
        Objects.requireNonNull(objectMapper, "objectMapper");
        Objects.requireNonNull(host, "host");
        if (host.isBlank()) throw new IllegalArgumentException("host cannot be blank");
        this.client = new MirrorNodeHttpClient(httpClient, objectMapper, host, headers);
        this.pollingInterval = Objects.requireNonNull(pollingInterval, "pollingInterval");
        this.maxRetries = maxRetries;
        this.limitPerPage = limitPerPage;
        this.scheduler = Objects.requireNonNull(scheduler, "scheduler");
    }

    @Override
    public Flowable<Block> replayPastBlocks(BigInteger start) throws IOException {
        BigInteger finalBlock = getCurrentBlockNumber();
        return createPublisher(() -> start).takeWhile(b -> b.number().compareTo(finalBlock) <= 0);
    }

    @Override
    public Flowable<Block> replayPastAndFutureBlocks(BigInteger start) {
        return createPublisher(() -> start);
    }

    @Override
    public Flowable<Block> replyFutureBlocks() throws IOException {
        BigInteger finalBlock = getCurrentBlockNumber();
        return createPublisher(() -> finalBlock);
    }

    @Override
    public Block getCurrentBlock() throws IOException {
        HttpUrl url =
                client.url(BASE_PATH + BLOCKS)
                        .newBuilder()
                        .addQueryParameter(QUERY_LIMIT, "1")
                        .addQueryParameter(QUERY_ORDER, "desc")
                        .build();
        BlockPageResponseModel resp = client.get(url, new TypeReference<>() {});
        return resp.getResults().isEmpty()
                ? null
                : BlockConverter.map(resp.getResults().getFirst(), List.of());
    }

    @Override
    public BigInteger getCurrentBlockNumber() throws IOException {
        Block b = getCurrentBlock();
        if (b == null) throw new EmptyResponseException("No blocks returned");
        return b.number();
    }

    @Override
    public Block getBlock(BigInteger number) throws IOException {
        return doFetchBlock(number);
    }

    @Override
    public Block getBlock(String hash) throws IOException {
        return doFetchBlock(hash);
    }

    @Override
    public List<Log> getLogs(BigInteger startBlock, BigInteger endBlock) throws IOException {
        return getLogs(startBlock, endBlock, null, List.of());
    }

    @Override
    public List<Log> getLogs(BigInteger startBlock, BigInteger endBlock, List<String> topics)
            throws IOException {
        return getLogs(startBlock, endBlock, null, topics);
    }

    @Override
    public List<Log> getLogs(BigInteger startBlock, BigInteger endBlock, String contractAddress)
            throws IOException {
        return getLogs(startBlock, endBlock, contractAddress, List.of());
    }

    @Override
    public List<Log> getLogs(
            BigInteger startBlock, BigInteger endBlock, String contractAddress, List<String> topics)
            throws IOException {
        BlockResponseModel startApi = doFetchNativeBlock(startBlock);
        BlockResponseModel endApi = doFetchNativeBlock(endBlock);
        return fetchLogs(
                startApi.timestamp().from(), endApi.timestamp().to(), contractAddress, topics);
    }

    @Override
    public List<Log> getLogs(String blockHash) throws IOException {
        return getLogs(blockHash, null);
    }

    @Override
    public List<Log> getLogs(String blockHash, String contractAddress) throws IOException {
        BlockResponseModel api = doFetchNativeBlock(blockHash);
        return fetchLogs(api.timestamp().from(), api.timestamp().to(), contractAddress, List.of());
    }

    @Override
    public Transaction getTransactionReceipt(String transactionHash) throws IOException {
        HttpUrl url = client.url(BASE_PATH + CONTRACT_RESULTS + "/" + transactionHash);
        return TransactionConverter.map(retryableGet(url, new TypeReference<>() {}));
    }

    private Flowable<Block> createPublisher(Supplier<BigInteger> startSupplier) {
        AtomicReference<BigInteger> current = new AtomicReference<>(startSupplier.get());
        return Flowable.create(
                emitter -> {
                    var task =
                            scheduler.scheduleAtFixedRate(
                                    () -> {
                                        try {
                                            BigInteger n = current.get();
                                            Block blk = doFetchBlock(n, true);
                                            emitter.onNext(blk);
                                            current.set(n.add(BigInteger.ONE));
                                        } catch (EmptyResponseException e) {
                                            log.debug("Waiting for block {}...", current.get());
                                        } catch (IOException e) {
                                            emitter.onError(e);
                                        }
                                    },
                                    0,
                                    pollingInterval.toMillis(),
                                    TimeUnit.MILLISECONDS);
                    emitter.setCancellable(() -> task.cancel(true));
                },
                BackpressureStrategy.DROP);
    }

    private <T> Block doFetchBlock(T id) throws IOException {
        return doFetchBlock(id, false);
    }

    private <T> Block doFetchBlock(T id, boolean txs) throws IOException {
        BlockResponseModel api = doFetchNativeBlock(id);
        List<ContractResultResponseModel> results =
                txs ? fetchContractResults(api.number()) : List.of();
        return BlockConverter.map(api, results.stream().map(TransactionConverter::map).toList());
    }

    private <T> BlockResponseModel doFetchNativeBlock(T id) throws IOException {
        HttpUrl url = client.url(BASE_PATH + BLOCKS + "/" + id);
        return retryableGet(url, new TypeReference<>() {});
    }

    private List<Log> fetchLogs(
            String startTs, String endTs, String contractAddress, List<String> topics)
            throws IOException {
        var all = new ArrayList<Log>();
        String path =
                contractAddress == null
                        ? BASE_PATH + LOGS
                        : BASE_PATH + LOGS_FOR_CONTRACT.replace("{contract}", contractAddress);
        HttpUrl.Builder builder =
                client.url(path)
                        .newBuilder()
                        .addQueryParameter(QUERY_ORDER, "asc")
                        .addQueryParameter(QUERY_LIMIT, String.valueOf(limitPerPage))
                        .addQueryParameter(QUERY_TIMESTAMP, "gte:" + startTs)
                        .addQueryParameter(QUERY_TIMESTAMP, "lte:" + endTs);
        for (String topic : topics) {
            builder.addQueryParameter(TOPIC0, topic);
        }

        while (true) {
            LogPageResponseModel page = retryableGet(builder.build(), new TypeReference<>() {});
            page.getResults().stream().map(LogConverter::map).forEach(all::add);
            String next = page.getLinks().get(LINK_NEXT);
            if (next == null) break;
            builder = client.url(next).newBuilder();
        }
        return all;
    }

    private List<ContractResultResponseModel> fetchContractResults(BigInteger blockNumber)
            throws IOException {
        var all = new ArrayList<ContractResultResponseModel>();
        HttpUrl.Builder builder = client.url(BASE_PATH + CONTRACT_RESULTS).newBuilder();
        builder.addQueryParameter(QUERY_BLOCK_NUMBER, blockNumber.toString())
                .addQueryParameter(QUERY_LIMIT, String.valueOf(limitPerPage));
        while (true) {
            ContractResultListResponseModel page =
                    retryableGet(builder.build(), new TypeReference<>() {});
            all.addAll(page.getResults());
            String next = page.getLinks().get(LINK_NEXT);
            if (next == null) break;
            builder = client.url(next).newBuilder();
        }
        return all;
    }

    private <A> A retryableGet(HttpUrl url, TypeReference<A> typeRef) throws IOException {
        int attempts = 0;
        long backoff = 500;
        while (true) {
            try {
                return client.get(url, typeRef);
            } catch (EmptyResponseException | IOException e) {
                if (++attempts > maxRetries) {
                    throw new UnexpectedResponseException("Retries exhausted for " + url, e);
                }
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Interrupted during backoff", ie);
                }
                backoff = Math.min(backoff * 2, 5000);
            }
        }
    }
}
