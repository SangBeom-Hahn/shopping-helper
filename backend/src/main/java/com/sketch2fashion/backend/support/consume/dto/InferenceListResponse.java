package com.sketch2fashion.backend.support.consume.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class InferenceListResponse {

    private String thumbnailUrl;

    private String webSearchUrl;

    private String hostPageUrl;

    private String site;
}
