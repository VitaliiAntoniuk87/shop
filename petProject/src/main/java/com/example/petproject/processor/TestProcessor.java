package com.example.petproject.processor;

import com.example.petproject.constants.AppConstants;

public class TestProcessor extends BatchProcessor {
    @Override
    public void run() {
        System.out.println("test processor running");
        System.out.println("LOGS_CLEAN_UP_PROCESSOR_ACTIVATED: " + AppConstants.LOGS_CLEAN_UP_PROCESSOR_ACTIVATED);
        System.out.println("LOG_FILE_SIZE_LIMIT_BYTES: " + AppConstants.LOG_FILE_SIZE_LIMIT_BYTES);
    }
}
