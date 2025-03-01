package com.sketch2fashion.backend.utils;

public abstract class SketchConstants {

    // FILE
    public static final String PATH = "ztyle/upload/";
    public static final String STORE_PATH_FORMAT = "%s%s%s";
    public static final String BASIC_SKETCH_PATH = "classpath:static/basic/%s.png";
    public static final String ATTACH_FILENAME = "attachment; filename=%s";
    public static final String IDX_STAND = ".";

    // MESSAGE
    public static final String QUEUE_PUBLISH_THREAD_NAME = "QueuePublish-";

    public static final String SEARCH_RESULT_CACHE = "SEARCH_RESULT_CACHE";

    public static final String CACHE_MANAGER_NAME = "cacheManager";

    public static final String KEY_PREFIX = "SEARCH_RESULT_CACHE::";

    public static final String MODEL_RUN_CONSUMER_GROUP = "default-group";

    public static final String MODEL_RUN_CONSUMER_NAME = "model-consumer";

    // INTERCEPTOR
    public static final int MESSAGE_INDEX = 0;
    public static final String APP_JSON = "application/json";
    public static final int EMPTY = 0;
    public static final String RESPONSE_BODY_KEY = "id";
    public static final String REQUEST_BODY_KEY = "objectType";
}
