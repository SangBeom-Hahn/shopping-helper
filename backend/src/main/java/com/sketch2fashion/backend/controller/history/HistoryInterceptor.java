package com.sketch2fashion.backend.controller.history;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketch2fashion.backend.controller.dto.ClothesSaveRequest;
import com.sketch2fashion.backend.domain.history.CommonLog;
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
        if (handler instanceof HandlerMethod handlerMethod) {
            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

            if (hasHttpMessage(methodParameters)) {
                MethodParameter methodParameter = methodParameters[MESSAGE_INDEX];

                if (isUploadRequest(methodParameter)) {
                    final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;
                    if (isCorrectContentType(cachingResponse)) {
                        if (isNotEmpty(cachingResponse)) {
                            saveUploadHistory(request, cachingResponse);
                        }
                    }
                }
            } else {
                saveUserTransactionHistory(request);
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

    private boolean isUploadRequest(MethodParameter methodParameter) {
        return methodParameter.getParameterType()
                .equals(ClothesSaveRequest.class);
    }

    private void saveUserTransactionHistory(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        SiteName siteName = SiteName.from(requestUri);
        transactionHistoryRepository.save(new TransactionLog(requestUri, siteName));
    }

    private void saveUploadHistory(HttpServletRequest request, ContentCachingResponseWrapper cachingResponse) throws IOException {
        Long messageId = objectMapper.readTree(cachingResponse.getContentAsByteArray())
                .get(RESPONSE_BODY_KEY)
                .asLong();
        String requestUri = request.getRequestURI();

        transactionHistoryRepository.save(new TransactionLog(messageId, requestUri));
        commonHistoryRepository.save(
                CommonLog.builder()
                        .messageId(messageId)
                        .searchCategoryType(request.getParameter(REQUEST_BODY_KEY))
                        .build()
        );
    }
}
