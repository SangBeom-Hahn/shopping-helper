package com.sketch2fashion.backend.support.consume.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InferenceListResponse {

    private String thumbnailUrl;

    private String webSearchUrl;

    private String hostPageUrl;

    private String name;

    private String site;

    public static InferenceListResponse of(
            final String thumbnailUrl,
            final String webSearchUrl,
            final String hostPageUrl,
            final String name,
            final String site
    ) {
        return new InferenceListResponse(thumbnailUrl, webSearchUrl, hostPageUrl, site, name);
    }
}
