package com.sketch2fashion.backend.support.consume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketch2fashion.backend.exception.HealthCheckException;
import com.sketch2fashion.backend.exception.InferenceFailException;
import com.sketch2fashion.backend.service.ResultService;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.support.ClothesModelHttpCaller;
import com.sketch2fashion.backend.support.ModelSearcher;
import com.sketch2fashion.backend.support.SearchConverter;
import com.sketch2fashion.backend.support.consume.dto.InferenceRequest;
import com.sketch2fashion.backend.support.consume.dto.InferencesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@RequiredArgsConstructor
public class ClothesJobConsumerListener implements StreamListener<String, ObjectRecord<String, MessageResponseDto>> {

    private final ClothesModelHttpCaller clothesModelHttpCaller;
    private final ObjectMapper objectMapper;
    private final ResultService resultService;

    @Override
    public void onMessage(final ObjectRecord<String, MessageResponseDto> message) {
        try {
            final MessageResponseDto messageResponseDto = message.getValue();
            final String modelPath = ModelSearcher.searchModel(messageResponseDto.getObjectType());
            validateUrlS(modelPath);

            final CloseableHttpResponse response = inferenceProcess(modelPath, messageResponseDto);
            final StatusCode statusCode = StatusCode.from(response.getStatusLine().getStatusCode());

            if(statusCode.isOk()) {
                final InferencesResponse inferenceResponse = SearchConverter.convertResponse(response);
                resultService.saveResult(messageResponseDto.getId(), inferenceResponse);
            } else {
                throw new InferenceFailException();
            }

        } catch (JsonProcessingException e) {
            throw new InferenceFailException();
        }
    }

    private void validateUrlS(final String url) {
        try {
            final URL dest = new URL(url);
            final HttpURLConnection connection = (HttpURLConnection) dest.openConnection();
            connection.setUseCaches(false);
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);

            if (canNotConnect(connection.getResponseCode())) {
                throw new HealthCheckException();
            }
        } catch (IOException e) {
            throw new HealthCheckException();
        }
    }

    private boolean canNotConnect(final int responseStatus) throws IOException {
        return HttpStatus.OK.value() != responseStatus;
    }

    private CloseableHttpResponse inferenceProcess(final String modelPath, final MessageResponseDto messageResponseDto) throws JsonProcessingException {
        final InferenceRequest requestBody = InferenceRequest.from(messageResponseDto);
        return clothesModelHttpCaller.callModel(
                modelPath,
                objectMapper.writeValueAsString(requestBody)
        );
    }
}
