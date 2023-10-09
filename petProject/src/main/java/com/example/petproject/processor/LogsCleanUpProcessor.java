package com.example.petproject.processor;

import com.example.petproject.constants.AppConstants;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
@Log4j2
public class LogsCleanUpProcessor extends BatchProcessor {

    private static final long DELAY_MINUTES = AppConstants.LOGS_CLEAN_UP_PROCESSOR_DELAY_MINUTES;

    @Override
    public void run() {
        logCleaner();
    }

    @Override
    public long getDelay() {
        return DELAY_MINUTES;
    }

    private void logCleaner() {
        Path path = Paths.get(AppConstants.LOG_FILES_PATH);
        log.info("LogCleaner is running");
        try {
            if (Files.exists(path) && Files.isDirectory(path)) {
                log.info("dir path was successfully validated");
                Files.list(path)
                        .filter(f -> {
                            try {
                                return Files.size(f) > AppConstants.LOG_FILE_SIZE_LIMIT_TO_CLEAR_BYTES;
                            } catch (IOException e) {
                                log.error("Error while checking file size");
                                throw new RuntimeException(e);
                            }
                        })
                        .forEach(f -> {
                            try {
                                Files.write(f, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
                                log.info("Log file " + f.getFileName() + " was cleared");
                            } catch (IOException e) {
                                log.error("Error while clearing log file");
                                throw new RuntimeException("Error while clearing log file", e);
                            }
                        });
            } else {
                log.error("Dir not found or Path is not a Dir");
                throw new FileNotFoundException("Dir not found or Path is not a Dir");
            }
        } catch (IOException e) {
            log.error("Error while processing log files");
            throw new RuntimeException("Error while processing log files", e);
        }
    }


}
