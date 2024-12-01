package com.sketch2fashion.backend.support;

import com.sketch2fashion.backend.exception.InferenceFailException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ClothesModelHttpCaller {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    private final CloseableHttpClient httpClient;

    public ClothesModelHttpCaller() {
        this.httpClient = HttpClients.createDefault();
    }

    public CloseableHttpResponse callModel(final String url, final String requestBody) {
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(CONTENT_TYPE, APPLICATION_JSON);
            httpPost.setEntity(new StringEntity(requestBody));

            return httpClient.execute(httpPost);
        } catch (IOException e) {
            throw new InferenceFailException();
        }
    }
}
