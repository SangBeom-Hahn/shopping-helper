package com.sketch2fashion.backend.controller.history;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketch2fashion.backend.domain.history.CommonLog;
import com.sketch2fashion.backend.domain.history.MethodName;
import com.sketch2fashion.backend.domain.history.SiteName;
import com.sketch2fashion.backend.domain.history.TransactionLog;
import com.sketch2fashion.backend.repository.history.CommonHistoryRepository;
import com.sketch2fashion.backend.repository.history.TransactionHistoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

import static com.sketch2fashion.backend.utils.SketchConstants.*;


@Component
@RequiredArgsConstructor
public class HistoryInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final CommonHistoryRepository commonHistoryRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        userHistoryChargeProcess(request, response, handler);
    }

    private void userHistoryChargeProcess(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (handler instanceof HandlerMethod handlerMethod) {
            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
            String requestUri = request.getRequestURI();
            String httpMethod = request.getMethod();

            handleHttpMessage(request, response, methodParameters, httpMethod, requestUri);
        }
    }

    private void handleHttpMessage(
            HttpServletRequest request,
            HttpServletResponse response,
            MethodParameter[] methodParameters,
            String httpMethod,
            String requestUri
    ) throws IOException {
        if (hasHttpMessage(methodParameters)) {
            MethodParameter methodParameter = methodParameters[MESSAGE_INDEX];
            handleHistory(request, response, methodParameter, httpMethod, requestUri);
        } else {
            saveUserTransactionHistory(httpMethod, requestUri);
        }
    }

    private void handleHistory(
            HttpServletRequest request,
            HttpServletResponse response,
            MethodParameter methodParameter,
            String httpMethod,
            String requestUri
    ) throws IOException {
        if (methodParameter.getMethod()!= null && MethodName.isUpload(methodParameter.getMethod().getName())) {
            uploadHistorySaveProcess(request, (ContentCachingResponseWrapper) response, httpMethod, requestUri);
        } else  {
            saveUserTransactionHistory(httpMethod, requestUri);
        }
    }

    private void uploadHistorySaveProcess(
            HttpServletRequest request,
            ContentCachingResponseWrapper response,
            String httpMethod,
            String requestUri
    ) throws IOException {
        if (isCorrectContentType(response)) {
            if (isNotEmpty(response)) {
                saveUploadHistory(httpMethod, requestUri, request.getParameter(REQUEST_BODY_KEY), response);
            }
        }
    }

    private boolean isNotEmpty(ContentCachingResponseWrapper cachingResponse) {
        return cachingResponse.getContentAsByteArray().length != EMPTY;
    }

    private boolean isCorrectContentType(ContentCachingResponseWrapper cachingResponse) {
        return cachingResponse.getContentType() != null && cachingResponse.getContentType().contains(APP_JSON);
    }

    private boolean hasHttpMessage(MethodParameter[] methodParameters) {
        return methodParameters.length >= 1;
    }

    private void saveUserTransactionHistory(String httpMethod, String requestUri) {
        SiteName siteName = SiteName.from(requestUri);
        transactionHistoryRepository.save(new TransactionLog(httpMethod, requestUri, siteName));
    }

    private void saveUploadHistory(
            String httpMethod,
            String requestUri,
            String searchCategoryType,
            ContentCachingResponseWrapper cachingResponse
    ) throws IOException {
        Long messageId = objectMapper.readTree(cachingResponse.getContentAsByteArray())
                .get(RESPONSE_BODY_KEY)
                .asLong();

        transactionHistoryRepository.save(new TransactionLog(messageId, httpMethod, requestUri));
        commonHistoryRepository.save(
                CommonLog.builder()
                        .messageId(messageId)
                        .searchCategoryType(searchCategoryType)
                        .build()
        );
    }
}
