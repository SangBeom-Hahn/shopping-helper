package com.sketch2fashion.backend.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchsResponseDto {

    private List<SearchResponseDto> searchResponseDtos;

    public static SearchsResponseDto from(final List<SearchResponseDto> searchResponseDtos) {
        return new SearchsResponseDto(searchResponseDtos);
    }
}
