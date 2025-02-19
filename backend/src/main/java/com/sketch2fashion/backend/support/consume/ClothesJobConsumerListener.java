package com.sketch2fashion.backend.support.consume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketch2fashion.backend.domain.modelresult.Status;
import com.sketch2fashion.backend.exception.AbsentFileException;
import com.sketch2fashion.backend.exception.HealthCheckException;
import com.sketch2fashion.backend.exception.InferenceFailException;
import com.sketch2fashion.backend.service.ResultService;
import com.sketch2fashion.backend.service.dto.MessageResponseDto;
import com.sketch2fashion.backend.support.ClothesModelHttpCaller;
import com.sketch2fashion.backend.support.ModelSearcher;
import com.sketch2fashion.backend.support.SearchConverter;
import com.sketch2fashion.backend.support.consume.dto.ErrorResponse;
import com.sketch2fashion.backend.support.consume.dto.InferenceRequest;
import com.sketch2fashion.backend.support.consume.dto.InferencesResponse;
import com.sketch2fashion.backend.support.publish.MessagePublisher;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.sketch2fashion.backend.domain.modelresult.Status.ERROR;
import static com.sketch2fashion.backend.domain.modelresult.Status.HEALTH_CHECK_ERROR;
import static com.sketch2fashion.backend.utils.SketchConstants.MODEL_RUN_CONSUMER_GROUP;

@Slf4j
@RequiredArgsConstructor
public class ClothesJobConsumerListener implements StreamListener<String, ObjectRecord<String, MessageResponseDto>> {

    private final ClothesModelHttpCaller clothesModelHttpCaller;
    private final ObjectMapper objectMapper;
    private final ResultService resultService;
    private final RedisTemplate<String, Object> streamRedisTemplate;

    @Override
    public void onMessage(final ObjectRecord<String, MessageResponseDto> message) {
        try {
            final MessageResponseDto messageResponseDto = message.getValue();
            final String modelPath = ModelSearcher.searchModel(messageResponseDto.getObjectType());
            validateUrlS(modelPath, messageResponseDto.getId());

            final CloseableHttpResponse response = inferenceProcess(modelPath, messageResponseDto);
            final StatusCode statusCode = StatusCode.from(response.getStatusLine().getStatusCode());

            handleInference(message, statusCode, response, messageResponseDto);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleInference(ObjectRecord<String, MessageResponseDto> message, StatusCode statusCode, CloseableHttpResponse response, MessageResponseDto messageResponseDto) {
        successProcess(message, statusCode, response, messageResponseDto);
        failProcess(message, statusCode, response, messageResponseDto);
    }

    private void successProcess(ObjectRecord<String, MessageResponseDto> message, StatusCode statusCode, CloseableHttpResponse response, MessageResponseDto messageResponseDto) {
        if(statusCode.isOk()) {
            final InferencesResponse inferenceResponse = SearchConverter.convertResponse(response);
            resultService.saveResult(messageResponseDto.getId(), inferenceResponse);
            streamRedisTemplate.opsForStream().acknowledge(MODEL_RUN_CONSUMER_GROUP, message);
        }
    }

    private void failProcess(ObjectRecord<String, MessageResponseDto> message, StatusCode statusCode, CloseableHttpResponse response, MessageResponseDto messageResponseDto) {
        if(statusCode.isBadGateWay()){
            ErrorResponse errorResponse = SearchConverter.convertErrorResponse(response);
            resultService.saveErrorResult(messageResponseDto.getId(), ERROR.getMessage());
            streamRedisTemplate.opsForStream().acknowledge(MODEL_RUN_CONSUMER_GROUP, message);
            throw new InferenceFailException(errorResponse.getError());
        }
    }

    private void validateUrlS(final String url, final Long messageId) {
        try {
            final URL dest = new URL(url);
            final HttpURLConnection connection = (HttpURLConnection) dest.openConnection();
            connection.setUseCaches(false);
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);

            if (canNotConnect(connection.getResponseCode())) {
                throw new HealthCheckException();
            }
        } catch (IOException | HealthCheckException e) {
                resultService.saveErrorResult(messageId, HEALTH_CHECK_ERROR.getMessage());
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
