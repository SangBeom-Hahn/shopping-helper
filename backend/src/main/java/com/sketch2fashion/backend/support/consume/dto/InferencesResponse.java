package com.sketch2fashion.backend.support.consume.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
public class InferencesResponse {

    private int farfetch;

    private int amazon;

    private int pinterest;

    private int etsy;

    private int walmart;

    private int ebay;

    private int noFilter;

    private List<InferenceListResponse> result;
}
