package com.sketch2fashion.backend.support;

import com.sketch2fashion.backend.domain.message.ObjectType;

import static com.sketch2fashion.backend.domain.message.ObjectType.*;
import static com.sketch2fashion.backend.utils.SketchConstants.*;

public class ModelSearcher {

    public static String searchModel(ObjectType objectType) {
        if(isTshirts(objectType)) {
            return TSHIRTS_WORKER_URI;
        } else if (isPants(objectType)) {
            return PANTS_WORKER_URI;
        } else if (isHat(objectType)) {
            return HAT_WORKER_URI;
        } else {
            return SKIRT_WORKER_URI;
        }
    }
}
