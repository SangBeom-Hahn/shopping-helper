package com.sketch2fashion.backend.support.consume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketch2fashion.backend.exception.HealthCheckException;
import com.sketch2fashion.backend.exception.InferenceFailException;
import com.sketch2fashion.backend.service.ResultService;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.support.SearchConverter;
import com.sketch2fashion.backend.support.ClothesModelHttpCaller;
import com.sketch2fashion.backend.support.ModelSearcher;
import com.sketch2fashion.backend.support.consume.dto.InferenceRequest;
import com.sketch2fashion.backend.support.consume.dto.InferencesResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@RequiredArgsConstructor
public class ClothesJobConsumerListener implements StreamListener<String, ObjectRecord<String, MessageResponseDto>> {

    private final ClothesModelHttpCaller clothesModelHttpCaller;
    private final ObjectMapper objectMapper;
    private final ResultService resultService;

    @Override
    public void onMessage(ObjectRecord<String, MessageResponseDto> message) {
        try {
            MessageResponseDto messageResponseDto = message.getValue();
            String modelPath = ModelSearcher.searchModel(messageResponseDto.getObjectType());

            // TODO: storeFilePath로 변경 예정
            CloseableHttpResponse response = inferenceProcess(modelPath, "./Untitled.png");
            StatusCode statusCode = StatusCode.from(response.getStatusLine().getStatusCode());

            // TODO: 추론 서버 예외처리
            if(statusCode.isOk()) {
                InferencesResponse inferenceResponse = SearchConverter.convertResponse(response);
            } else {
                throw new InferenceFailException();
            }

        } catch (JsonProcessingException e) {
            throw new InferenceFailException();
        }
    }

    private CloseableHttpResponse inferenceProcess(String modelPath, String storeFilePath) throws JsonProcessingException {
        InferenceRequest requestBody = InferenceRequest.from(storeFilePath);
        return clothesModelHttpCaller.callModel(
                modelPath,
                objectMapper.writeValueAsString(requestBody)
        );
    }
}
