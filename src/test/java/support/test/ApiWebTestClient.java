package support.test;

import org.assertj.core.util.Preconditions;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.URI;
import java.util.List;
import java.util.Objects;

public class ApiWebTestClient {
    public static final String BASE_URL = "http://localhost";
    private String baseUrl = BASE_URL;
    private int port;
    private WebTestClient.Builder testClientBuilder;

    private ApiWebTestClient(String baseUrl, int port) {
        this.baseUrl = baseUrl;
        this.port = port;
        this.testClientBuilder = WebTestClient
                .bindToServer()
                .baseUrl(String.format("%s:%d", baseUrl, port));
    }

    public static ApiWebTestClient of(int port) {
        Preconditions.checkArgument(Objects.nonNull(port), "Port ithis.arg$1s empty.");
        return new ApiWebTestClient(BASE_URL, port);
    }

    public <T> T getResource(String url, Class<T> clazz) {
        URI location = URI.create(url);
        return getResource(location, clazz);
    }

    public <T> T getResource(URI location, Class<T> clazz) {
        return testClientBuilder.build()
                .get()
                .uri(location.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(clazz)
                .returnResult().getResponseBody();
    }

    public <T> List<T> getResources(String url, Class<T> clazz) {
        URI location = URI.create(url);
        return testClientBuilder.build()
                .get()
                .uri(location.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(clazz)
                .returnResult().getResponseBody();
    }

    public WebTestClient getBuilder() {
        return testClientBuilder.build();
    }
}