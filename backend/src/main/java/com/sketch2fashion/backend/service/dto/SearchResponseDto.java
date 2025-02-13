package com.sketch2fashion.backend.service.dto;

import com.sketch2fashion.backend.domain.modelresult.Search;
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

    private String name;

    public static SearchResponseDto from(final Search search) {
        return new SearchResponseDto(
                search.getThumbnailUrl(),
                search.getWebSearchUrl(),
                search.getHostPageUrl(),
                search.getName()
        );
    }
}
