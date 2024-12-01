package com.sketch2fashion.backend.support.consume.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
            int farfetch,
            int amazon,
            int pinterest,
            int etsy,
            int walmart,
            int ebay,
            int noFilter,
            List<InferenceListResponse> result
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
