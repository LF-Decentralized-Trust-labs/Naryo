package io.librevents.infrastructure.node.interactor.hedera.http;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
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
}
