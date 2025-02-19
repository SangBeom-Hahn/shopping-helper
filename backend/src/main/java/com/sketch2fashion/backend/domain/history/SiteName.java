package com.sketch2fashion.backend.domain.history;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Getter
public enum SiteName {

    FARFETCH("/api/farfetch"),
    AMAZON("/api/amazon"),
    PINTEREST("/api/pinterest"),
    ETSY("/api/etsy"),
    WALMART("/api/walmart"),
    EBAY("/api/ebay"),
    MICROSOFT_BING("/api/noFilter"),
    NOT_SITE("NOT_SITE")
    ;

    private final String requestUri;

    SiteName(String requestUri) {
        this.requestUri = requestUri;
    }

    public static SiteName from(String requestUri) {
        return Arrays.stream(values())
                .filter(siteName -> siteName.getRequestUri().equals(requestUri))
                .findFirst()
                .orElse(NOT_SITE);
    }
}
