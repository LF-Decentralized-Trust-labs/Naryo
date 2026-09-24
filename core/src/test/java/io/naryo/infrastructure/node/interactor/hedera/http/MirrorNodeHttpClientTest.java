package io.naryo.infrastructure.node.interactor.hedera.http;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.naryo.application.node.interactor.block.dto.hedera.HederaTransaction;
import io.naryo.application.node.interactor.block.dto.hedera.TransactionName;
import io.naryo.infrastructure.node.interactor.hedera.map.TransactionConverter;
import io.naryo.infrastructure.node.interactor.hedera.response.TransactionListResponseModel;
import io.naryo.infrastructure.node.interactor.hedera.response.TransactionResponseModel;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MirrorNodeHttpClientTest {

    @Mock private OkHttpClient httpClient;
    @Mock private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        lenient().doReturn(mapper).when(mapper).copy();
    }

    @Test
    void constructor() {
        MirrorNodeHttpClient client =
                new MirrorNodeHttpClient(
                        this.httpClient, this.mapper, "http://localhost", Map.of());
        assertNotNull(client);
    }

    @Test
    void constructor_withNullValues() {
        assertThrows(
                NullPointerException.class,
                () -> new MirrorNodeHttpClient(null, this.mapper, "http://localhost", Map.of()));
        assertThrows(
                NullPointerException.class,
                () ->
                        new MirrorNodeHttpClient(
                                this.httpClient, null, "http://localhost", Map.of()));
        assertThrows(
                NullPointerException.class,
                () -> new MirrorNodeHttpClient(this.httpClient, this.mapper, null, Map.of()));
        assertThrows(
                IllegalArgumentException.class,
                () -> new MirrorNodeHttpClient(this.httpClient, this.mapper, "", Map.of()));
    }

    @Test
    void constructor_withBlankHost() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new MirrorNodeHttpClient(this.httpClient, this.mapper, " ", Map.of()));
    }

    @Test
    void constructor_withInvalidHost() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        new MirrorNodeHttpClient(
                                this.httpClient, this.mapper, "invalidhost", Map.of()));
    }

    @Test
    void constructor_withInvalidHeaders() {
        assertThrows(
                NullPointerException.class,
                () ->
                        new MirrorNodeHttpClient(
                                this.httpClient,
                                this.mapper,
                                "http://localhost",
                                Map.of("header", "value", "header2", null)));
    }

    @Test
    void url() {
        MirrorNodeHttpClient client =
                new MirrorNodeHttpClient(
                        this.httpClient, this.mapper, "http://localhost", Map.of());
        assertNotNull(client.url("/test"));
        assertEquals("http://localhost/test", client.url("/test").url().toString());
    }

    @Test
    void url_withNullPath() {
        MirrorNodeHttpClient client =
                new MirrorNodeHttpClient(
                        this.httpClient, this.mapper, "http://localhost", Map.of());
        assertThrows(NullPointerException.class, () -> client.url(null));
    }

    @Test
    void url_withEmptyPath() {
        MirrorNodeHttpClient client =
                new MirrorNodeHttpClient(
                        this.httpClient, this.mapper, "http://localhost", Map.of());
        assertThrows(IllegalArgumentException.class, () -> client.url(""));
    }

    @Test
    void get() throws IOException {
        HttpUrl url = HttpUrl.parse("http://localhost/test");
        MirrorNodeHttpClient client =
                new MirrorNodeHttpClient(
                        this.httpClient, this.mapper, "http://localhost", Map.of());
        Call call = mock();
        Response response = mock();
        ResponseBody responseBody = mock();
        ObjectReader reader = mock();
        TypeReference<String> typeRef = new TypeReference<>() {};
        doReturn(call).when(httpClient).newCall(any());
        doReturn(response).when(call).execute();
        doReturn(200).when(response).code();
        doReturn(responseBody).when(response).body();
        doReturn("response").when(responseBody).string();
        doReturn(reader).when(mapper).readerFor(typeRef);
        doReturn(reader).when(reader).withoutRootName();
        doReturn("response").when(reader).readValue(anyString());

        assertNotNull(client.get(url, typeRef));
    }

    /**
     * Two entries, verbatim, of the testnet mirror node listing for block 40929831
     * (/api/v1/transactions?timestamp=gte:1790264076.175779104&timestamp=lte:1790264078.251315104,
     * fetched on 2026-09-24). The first is a contract call charged 2164643443 tinybars (21.65
     * HBAR), which the mirror node serves as an int64; the second is a regular message submission
     * charged 315475 tinybars.
     */
    private static final String TRANSACTIONS_PAGE_WITH_FEE_ABOVE_INT_RANGE =
            """
                {
                  "transactions": [
                    {
                      "batch_key": null,
                      "bytes": null,
                      "charged_tx_fee": 2164643443,
                      "consensus_timestamp": "1790264078.036815187",
                      "entity_id": "0.0.9959",
                      "high_volume": false,
                      "high_volume_pricing_multiplier": 0,
                      "max_fee": "200000000",
                      "max_custom_fees": [],
                      "memo_base64": "",
                      "name": "CONTRACTCALL",
                      "nft_transfers": [],
                      "node": "0.0.6",
                      "nonce": 0,
                      "parent_consensus_timestamp": null,
                      "result": "SUCCESS",
                      "scheduled": false,
                      "staking_reward_transfers": [],
                      "token_transfers": [],
                      "transaction_hash": "YrhVM9zby3V7uLNZL8AEpc2EGhLKL1rQ6xM8hyDQPzSsjvLTrHdon6zrkQ6ISWEx",
                      "transaction_id": "0.0.7231440-1790264071-252511409",
                      "transfers": [
                        {
                          "account": "0.0.802",
                          "amount": 2164643443,
                          "is_approval": false
                        },
                        {
                          "account": "0.0.9829",
                          "amount": 1096344893,
                          "is_approval": false
                        },
                        {
                          "account": "0.0.7231440",
                          "amount": -3284704250,
                          "is_approval": false
                        },
                        {
                          "account": "0.0.10700141",
                          "amount": 23715914,
                          "is_approval": false
                        }
                      ],
                      "valid_duration_seconds": "120",
                      "valid_start_timestamp": "1790264071.252511409"
                    }
                  ],
                  "links": {
                    "next": null
                  }
                }
            """;

    private static final String TRANSACTIONS_PAGE_WITH_FEE_WITHIN_INT_RANGE =
            """
                {
                  "transactions": [
                    {
                      "batch_key": null,
                      "bytes": null,
                      "charged_tx_fee": 315475,
                      "consensus_timestamp": "1790264078.251315104",
                      "entity_id": "0.0.4739866",
                      "high_volume": false,
                      "high_volume_pricing_multiplier": 0,
                      "max_fee": "200000000",
                      "max_custom_fees": [],
                      "memo_base64": "",
                      "name": "CONSENSUSSUBMITMESSAGE",
                      "nft_transfers": [],
                      "node": "0.0.5",
                      "nonce": 0,
                      "parent_consensus_timestamp": null,
                      "result": "SUCCESS",
                      "scheduled": false,
                      "staking_reward_transfers": [],
                      "token_transfers": [],
                      "transaction_hash": "0HgfscP+X0FrKWj0kLEnk78Ok5X1Pal2gYy4MO4FSuMNSjXRu5ybi+acq40DUzK5",
                      "transaction_id": "0.0.4739863-1790264065-595142011",
                      "transfers": [
                        {
                          "account": "0.0.802",
                          "amount": 315475,
                          "is_approval": false
                        },
                        {
                          "account": "0.0.4739863",
                          "amount": -315475,
                          "is_approval": false
                        }
                      ],
                      "valid_duration_seconds": "120",
                      "valid_start_timestamp": "1790264065.595142011"
                    }
                  ],
                  "links": {
                    "next": null
                  }
                }
            """;

    @Test
    void get_deserializesTransactionsWithChargedTxFeeAboveIntRange() throws IOException {
        TransactionListResponseModel page =
                getTransactions(TRANSACTIONS_PAGE_WITH_FEE_ABOVE_INT_RANGE);

        assertEquals(1, page.getResults().size());
        TransactionResponseModel model = page.getResults().getFirst();
        assertEquals(TransactionName.CONTRACTCALL, model.name());
        assertEquals("0.0.7231440-1790264071-252511409", model.transactionId());
        assertEquals(2164643443L, model.chargedTxFee().longValue());
        assertEquals(BigInteger.valueOf(-3284704250L), model.transfers().get(2).amount());

        HederaTransaction transaction =
                TransactionConverter.map(model, BigInteger.valueOf(40929831));

        assertEquals(2164643443L, transaction.getChargedTxFee().longValue());
        assertEquals("0.0.7231440", transaction.getFrom());
    }

    @Test
    void get_deserializesTransactionsWithChargedTxFeeWithinIntRange() throws IOException {
        TransactionListResponseModel page =
                getTransactions(TRANSACTIONS_PAGE_WITH_FEE_WITHIN_INT_RANGE);

        assertEquals(1, page.getResults().size());
        TransactionResponseModel model = page.getResults().getFirst();
        assertEquals(TransactionName.CONSENSUSSUBMITMESSAGE, model.name());
        assertEquals(315475L, model.chargedTxFee().longValue());

        HederaTransaction transaction =
                TransactionConverter.map(model, BigInteger.valueOf(40929831));

        assertEquals(315475L, transaction.getChargedTxFee().longValue());
    }

    private TransactionListResponseModel getTransactions(String body) throws IOException {
        ObjectMapper objectMapper =
                new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MirrorNodeHttpClient client =
                new MirrorNodeHttpClient(
                        this.httpClient, objectMapper, "http://localhost", Map.of());
        Call call = mock();
        Response response = mock();
        ResponseBody responseBody = mock();
        doReturn(call).when(httpClient).newCall(any());
        doReturn(response).when(call).execute();
        doReturn(200).when(response).code();
        doReturn(responseBody).when(response).body();
        doReturn(body).when(responseBody).string();

        return client.get(
                client.url(
                        "/api/v1/transactions?timestamp=gte:1790264076.175779104&timestamp=lte:1790264078.251315104&limit=100"),
                new TypeReference<>() {});
    }
}
