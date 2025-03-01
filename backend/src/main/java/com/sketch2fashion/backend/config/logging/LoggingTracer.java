package com.sketch2fashion.backend.config.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingTracer {

    private static final String TRACE_DEPTH_SPACE = "--";
    private final LoggingStatusManager loggingStatusManager;

    public void begin(final String message, final Object[] args) {
        loggingStatusManager.syncStatus();
        if (log.isDebugEnabled()) {
            log.debug(
                    "[{}] {}--> {} args={}",
                    loggingStatusManager.getTaskId(),
                    getDepthSpace(loggingStatusManager.getDepthLevel()),
                    message,
                    args
            );
        }
    }

    public void end(final String message) {
        if (log.isDebugEnabled()) {
            final long stopTimeMillis = System.currentTimeMillis();
            final long resultTimeMillis = stopTimeMillis - loggingStatusManager.getStartTimeMillis();
            log.debug(
                    "[{}] {}<-- {} time={}ms",
                    loggingStatusManager.getTaskId(),
                    getDepthSpace(loggingStatusManager.getDepthLevel()),
                    message,
                    resultTimeMillis
            );
        }

        loggingStatusManager.release();
    }

    public void exception(final String message, final Exception e) {
        if (log.isDebugEnabled()) {
            final long stopTimeMillis = System.currentTimeMillis();
            final long resultTimeMillis = stopTimeMillis - loggingStatusManager.getStartTimeMillis();
            log.debug(
                    "[{}] <X-{} {} time={}ms ex={}",
                    loggingStatusManager.getTaskId(),
                    getDepthSpace(loggingStatusManager.getDepthLevel()),
                    message,
                    resultTimeMillis,
                    e.toString()
            );
        }
        loggingStatusManager.release();
    }

    private String getDepthSpace(final int depthLevel) {
        return TRACE_DEPTH_SPACE.repeat(depthLevel);
    }
}
