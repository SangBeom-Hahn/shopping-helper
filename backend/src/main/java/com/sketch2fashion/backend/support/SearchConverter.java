package com.sketch2fashion.backend.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketch2fashion.backend.exception.InferenceFailException;
import com.sketch2fashion.backend.support.consume.dto.InferencesResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SearchConverter {

    public static InferencesResponse convertResponse(final CloseableHttpResponse response) {
        validateResponse(response);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            String inputLine;
            StringBuilder sb = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            return new ObjectMapper().readValue(sb.toString(), InferencesResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void validateResponse(final CloseableHttpResponse response) {
        if (response == null) {
            throw new InferenceFailException();
        }
    }
}