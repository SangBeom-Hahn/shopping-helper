package com.sketch2fashion.backend.support.consume.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InferenceListResponse {

    private String thumbnailUrl;

    private String webSearchUrl;

    private String hostPageUrl;

    private String site;

    public static InferenceListResponse of(
            String thumbnailUrl,
            String webSearchUrl,
            String hostPageUrl,
            String site
    ) {
        return new InferenceListResponse(thumbnailUrl, webSearchUrl, hostPageUrl, site);
    }
}
