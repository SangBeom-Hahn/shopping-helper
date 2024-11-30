package com.sketch2fashion.backend.support;

import com.sketch2fashion.backend.domain.message.ObjectType;

import static com.sketch2fashion.backend.domain.message.ObjectType.*;

public class ModelSearcher {

    public static String searchModel(ObjectType objectType) {
        if(isTshirts(objectType)) {
            return "http://127.0.0.1:5000";
        } else if (isPants(objectType)) {
            return "http://127.0.0.1:5001";
        } else if (isHat(objectType)) {
            return "http://127.0.0.1:5002";
        } else {
            return "http://127.0.0.1:5003";
        }
    }
}
