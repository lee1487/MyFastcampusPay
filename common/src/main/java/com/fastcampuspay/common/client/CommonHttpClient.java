package com.fastcampuspay.common.client;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommonHttpClient {
    private static final long READ_TIMEOUT_SECONDS = 1800L; /*60 * 30*/
    private static final long CONNECT_TIMEOUT_SECONDS = 5L;
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT_SECONDS))
            .build();

    private static final String ACCEPT = "Accept";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CACHE_CONTROL = "Cache-Control";

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public HttpResponse<String> sendPostRequest(Map<String, String> body, String url) {
        try {
            String form = body.entrySet()
                    .stream()
                    .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .timeout(Duration.ofSeconds(READ_TIMEOUT_SECONDS))
                    .header(CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .header(ACCEPT, "application/json")
                    .build();

            return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("HTTP 요청 실패: " + url, e);
        }
    }

    public HttpResponse<String> sendGetRequest(String url) {
        return sendGetRequest(url, null);
    }

    public HttpResponse<String> sendGetRequest(String url, Map<String, String> headers) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .timeout(Duration.ofSeconds(READ_TIMEOUT_SECONDS))
                    .header(ACCEPT, "application/json");

            if (headers != null) {
                headers.forEach(builder::header);
            }

            HttpRequest httpRequest = builder.build();
            return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("HTTP 요청 실패: " + url, e);
        }
    }

    public HttpResponse<String> sendRequest(String url, String method) {
        return sendRequest(url, method, null);
    }

    public HttpResponse<String> sendRequest(String url, String method, String body) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .timeout(Duration.ofSeconds(READ_TIMEOUT_SECONDS))
                    .header(ACCEPT, "application/json");

            if (body != null) {
                builder.header(CONTENT_TYPE, "application/json");
            }

            HttpRequest.BodyPublisher bodyPublisher = body != null
                    ? HttpRequest.BodyPublishers.ofString(body)
                    : HttpRequest.BodyPublishers.noBody();

            HttpRequest httpRequest;
            String upperMethod = method.toUpperCase();
            switch (upperMethod) {
                case "GET":
                    httpRequest = builder.GET().build();
                    break;
                case "POST":
                    httpRequest = builder.POST(bodyPublisher).build();
                    break;
                case "PUT":
                    httpRequest = builder.PUT(bodyPublisher).build();
                    break;
                case "DELETE":
                    httpRequest = builder.DELETE().build();
                    break;
                default:
                    httpRequest = builder.method(method, bodyPublisher).build();
                    break;
            }

            return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("HTTP 요청 실패: " + url, e);
        }
    }
}
