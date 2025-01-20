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

    // ML
    public static final String TSHIRTS_WORKER_URI = "http://127.0.0.1:5000";
    public static final String PANTS_WORKER_URI = "http://127.0.0.1:5001";
    public static final String HAT_WORKER_URI = "http://127.0.0.1:5002";
    public static final String SKIRT_WORKER_URI = "http://127.0.0.1:5003";
}
