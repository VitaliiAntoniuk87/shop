package com.example.petproject.constants;

import org.springframework.stereotype.Component;

@Component
public class AppConstants {

    public static final boolean CART_CLEAN_UP_PROCESSOR_ACTIVATED = true;
    public static final long CART_AUTO_CANCELLATION_TIMEOUT_MINUTES = 2880;
    public static final boolean LOGS_CLEAN_UP_PROCESSOR_ACTIVATED = true;
    public static final long LOGS_CLEAN_UP_PROCESSOR_TIMEOUT_MINUTES = 1440;
    public static final long LOG_FILE_SIZE_LIMIT_BYTES = 10_000_000_000L;
    public static final String LOG_FILES_PATH = "/log";
    public static final boolean TEST_PROCESSOR_ACTIVATED = true;


}
