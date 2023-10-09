package com.example.petproject.processor;

import java.util.concurrent.TimeUnit;


public abstract class BatchProcessor implements Runnable {

    private static final long INITIAL_DELAY_MINUTES = 1;
    private static final long DELAY_MINUTES = 3;
    private static final TimeUnit PROCESSOR_TIME_UNIT = TimeUnit.MINUTES;

    public long getInitialDelay() {
        return INITIAL_DELAY_MINUTES;
    }

    public long getDelay() {
        return DELAY_MINUTES;
    }

    public TimeUnit getProcessorTimeUnit() {
        return PROCESSOR_TIME_UNIT;
    }
}

