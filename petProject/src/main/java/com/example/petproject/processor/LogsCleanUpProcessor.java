package com.example.petproject.processor;

import com.example.petproject.constants.AppConstants;
import lombok.AllArgsConstructor;
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

    private static final long DELAY_MINUTES = AppConstants.LOGS_CLEAN_UP_PROCESSOR_TIMEOUT_MINUTES;

    @Override
    public void run() {
        log.info("logCleaner is running");
        logCleaner();
    }

    private void logCleaner() {
        Path path = Paths.get(AppConstants.LOG_FILES_PATH);
        log.info("entering logCleaner");
        try {
            if (Files.exists(path) && Files.isDirectory(path)) {
                log.info("dir path was validated");
                Files.list(path)
                        .filter(f ->
                                {
                                    try {
                                        boolean sizeExceedsLimit = Files.size(f) > AppConstants.LOG_FILE_SIZE_LIMIT_BYTES;
                                        if (sizeExceedsLimit) {
                                            log.info("Размер файла больше лимита: " + AppConstants.LOG_FILE_SIZE_LIMIT_BYTES);
                                        }
                                        return sizeExceedsLimit;
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        )
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
                throw new FileNotFoundException("Dir not found or Path is not a Dir");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while processing log files", e);
        }

    }


}
