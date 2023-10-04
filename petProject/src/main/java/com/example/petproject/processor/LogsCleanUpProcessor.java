package com.example.petproject.processor;

import com.example.petproject.constants.AppConstants;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
@Log4j2
public class LogsCleanUpProcessor extends BatchProcessor {

    private static final long DELAY_MINUTES = AppConstants.LOGS_CLEAN_UP_PROCESSOR_TIMEOUT_MINUTES;

    @Override
    public void run() {
//        logCleaner(AppConstants.LOG_FILES_PATH)
    }

//    private void logCleaner(String dirPath, long fileSizeLimit) {
//        Path path = Paths.get(dirPath);
//        if (Files.exists(path)) {
//            if (Files.isDirectory(path)) {
//                try {
//                    Files.list(path)
//                            .filter(f -> {
//                                try {
//                                    return Files.size(f) > fileSizeLimit;
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            })
//                            .forEach(f -> {
//                                try (BufferedWriter writer = Files.newBufferedWriter(f, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
//
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
//
//                            });
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        } else {
//            try {
//                throw new FileNotFoundException();
//            } catch (FileNotFoundException e) {
//                log.error("File/Dir not found");
//            }
//        }
//
//
//    }


}
