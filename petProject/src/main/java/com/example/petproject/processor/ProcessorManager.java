package com.example.petproject.processor;

import com.example.petproject.constants.AppConstants;
import com.example.petproject.service.CartService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
@Log4j2
@AllArgsConstructor
public class ProcessorManager {


    private final CartService cartService;

    private final List<ScheduledExecutorService> executorList = new ArrayList<>();

    synchronized public void init() {
        if (!executorList.isEmpty()) {
            log.error("ProcessorManager: Attempt to concurrent access!");
            return;
        }

        if (AppConstants.CART_CLEAN_UP_PROCESSOR_ACTIVATED) {
            executorList.add(execute(new CartCleanUpProcessor(cartService)));
        }

        if (AppConstants.TEST_PROCESSOR_ACTIVATED) {
            executorList.add(execute(new TestProcessor()));
        }

        if (AppConstants.LOGS_CLEAN_UP_PROCESSOR_ACTIVATED) {
            log.info("LogsCleanUpProcessor is running");
            executorList.add(execute(new LogsCleanUpProcessor()));
        }

        log.info("ProcessorManager was initialized");
    }

    private ScheduledExecutorService execute(BatchProcessor processor) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(processor, processor.getInitialDelay(), processor.getDelay(), processor.getProcessorTimeUnit());
        return executor;
    }

}
