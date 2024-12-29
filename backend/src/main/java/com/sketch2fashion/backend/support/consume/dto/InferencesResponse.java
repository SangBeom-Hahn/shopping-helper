package com.sketch2fashion.backend.support.consume.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InferencesResponse {

    private int farfetch;

    private int amazon;

    private int pinterest;

    private int etsy;

    private int walmart;

    private int ebay;

    private int noFilter;

    private List<InferenceListResponse> result;

    public static InferencesResponse of(
            final int farfetch,
            final int amazon,
            final int pinterest,
            final int etsy,
            final int walmart,
            final int ebay,
            final int noFilter,
            final List<InferenceListResponse> result
    ) {
        return new InferencesResponse(
                farfetch,
                amazon,
                pinterest,
                etsy,
                walmart,
                ebay,
                noFilter,
                result
        );
    }
}
