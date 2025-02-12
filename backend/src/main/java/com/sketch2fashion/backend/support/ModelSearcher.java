package com.sketch2fashion.backend.support;

import com.sketch2fashion.backend.config.ModelConfig;
import com.sketch2fashion.backend.domain.message.ObjectType;
import org.springframework.beans.factory.annotation.Value;

import static com.sketch2fashion.backend.domain.message.ObjectType.*;
import static com.sketch2fashion.backend.utils.SketchConstants.*;

public class ModelSearcher {

    public static String searchModel(final ObjectType objectType) {
        if(isTshirts(objectType)) {
            return ModelConfig.TSHIRTS_WORKER_URI;
        } else if (isPants(objectType)) {
            return ModelConfig.PANTS_WORKER_URI;
        } else if (isHat(objectType)) {
            return ModelConfig.HAT_WORKER_URI;
        } else {
            return ModelConfig.SKIRT_WORKER_URI;
        }
    }
}
