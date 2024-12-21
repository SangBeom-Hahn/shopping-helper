package com.sketch2fashion.backend.service.dto;

import com.sketch2fashion.backend.domain.modelresult.Search;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchResponseDto {

    private String thumbnailUrl;

    private String webSearchUrl;

    private String hostPageUrl;

    public static SearchResponseDto from(Search search) {
        return new SearchResponseDto(
                search.getThumbnailUrl(),
                search.getWebSearchUrl(),
                search.getHostPageUrl()
        );
    }
}
