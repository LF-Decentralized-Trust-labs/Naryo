package io.naryo.infrastructure.node.interactor.hedera.http;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.naryo.infrastructure.node.interactor.hedera.response.ContractResultListResponseModel;
import io.naryo.infrastructure.node.interactor.hedera.response.ContractResultResponseModel;
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

    @Test
    void get_deserializesContractResultsWithEmptyAccessList() throws IOException {
        ContractResultListResponseModel page = getContractResults(contractResultsPage("[]"));

        assertEquals(1, page.getResults().size());
        ContractResultResponseModel result = page.getResults().getFirst();
        assertEquals(
                "0x23dd1d9c21eaa8d98ba3a898aa9138934ecbfef56bccb0db37f4dcc5667660da",
                result.hash());
        assertEquals("40930397", result.blockNumber());
        assertEquals(BigInteger.valueOf(131701), result.gasUsed());
        assertEquals("0x1", result.status());
        assertEquals(
                "/api/v1/contracts/results?limit=1&order=desc&timestamp=lt:1790265255.117000104",
                page.getLinks().get("next"));
    }

    @Test
    void get_deserializesContractResultsWithAccessListEntries() throws IOException {
        String accessList =
                """
                [{"address":"0xa02457e5dfd32bda5fc7e1f1b008aa5979568150","storage_keys":["0x0000000000000000000000000000000000000000000000000000000000000081"]}]""";

        ContractResultListResponseModel page = getContractResults(contractResultsPage(accessList));

        assertEquals(1, page.getResults().size());
        assertNotNull(page.getResults().getFirst().accessList());
    }

    private ContractResultListResponseModel getContractResults(String body) throws IOException {
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
                client.url("/api/v1/contracts/results?limit=1&order=desc"),
                new TypeReference<>() {});
    }

    private static String contractResultsPage(String accessList) {
        return """
                {"results":[{"address":"0xf76f36a125cfeb26815d2990c7808122e93de206","amount":0,"bloom":"0x00000000000000000000000008000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002000000040010040000000000000000000000000004000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000040000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000200000","call_result":"0x","contract_id":"0.0.10255430","created_contract_ids":[],"error_message":null,"from":"0x00000000000000000000000000000000004f0b69","function_parameters":"0xdee0f5e5bb16c3066bf91e6f8df1181d0f92679f2fc5e1e63f8c6e7ec6f31f3b112cd5230000000000000000000000000000000000000000000000000007b8e69eabc083","gas_consumed":131701,"gas_limit":300000,"gas_used":131701,"timestamp":"1790265255.117000104","to":"0x00000000000000000000000000000000009c7c46","hash":"0x23dd1d9c21eaa8d98ba3a898aa9138934ecbfef56bccb0db37f4dcc5667660da","block_hash":"0xd047c6184ce323dba9c6c207651245cd96a82d920fdb283fa3f5294b7b850e65f176aff1204882acdb0fa129c7e06c64","block_number":40930397,"result":"SUCCESS","transaction_index":2,"status":"0x1","failed_initcode":null,"access_list":%s,"block_gas_used":389099,"chain_id":"0x128","gas_price":"0x6d","max_fee_per_gas":null,"max_priority_fee_per_gas":null,"r":null,"s":null,"type":0,"v":null,"nonce":null}],"links":{"next":"/api/v1/contracts/results?limit=1&order=desc&timestamp=lt:1790265255.117000104"}}
                """
                .formatted(accessList);
    }
}
