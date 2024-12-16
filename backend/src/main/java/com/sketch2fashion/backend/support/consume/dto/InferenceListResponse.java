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

    private String name;

    private String site;

    public static InferenceListResponse of(
            String thumbnailUrl,
            String webSearchUrl,
            String hostPageUrl,
            String name,
            String site
    ) {
        return new InferenceListResponse(thumbnailUrl, webSearchUrl, hostPageUrl, site, name);
    }
}
