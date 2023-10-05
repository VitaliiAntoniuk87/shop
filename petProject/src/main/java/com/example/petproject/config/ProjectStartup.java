package com.example.petproject.config;

import com.example.petproject.constants.AppConstants;
import com.example.petproject.processor.ProcessorManager;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProjectStartup {

    private final ProcessorManager processorManager;

    @PostConstruct
    private void init() {
        AppConstants.init();
        processorManager.init();
    }
}
