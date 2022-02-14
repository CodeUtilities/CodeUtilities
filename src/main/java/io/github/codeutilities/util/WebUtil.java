package io.github.codeutilities.util;

import io.github.codeutilities.CodeUtilities;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

public class WebUtil {

    public static void getAsync(String url, Consumer<String> callback) {
        HttpClient client = HttpClient.newHttpClient();
        client.sendAsync(HttpRequest.newBuilder(URI.create(url)).GET().build(), HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(s -> {
                CodeUtilities.MC.submit(() -> {
                    callback.accept(s);
                });
            });
    }

}
