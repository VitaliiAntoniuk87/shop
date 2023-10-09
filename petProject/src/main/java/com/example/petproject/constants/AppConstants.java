package com.example.petproject.constants;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Component
@Log4j2
@AllArgsConstructor
public class AppConstants {

    public static boolean CART_CLEAN_UP_PROCESSOR_ACTIVATED = false;
    public static long NEW_CART_TIMEOUT_TO_AUTO_CANCELLATION_AFTER_CREATION_MINUTES = 2880;
    public static boolean LOGS_CLEAN_UP_PROCESSOR_ACTIVATED = false;
    public static long LOGS_CLEAN_UP_PROCESSOR_DELAY_MINUTES = 1440;
    public static long LOG_FILE_SIZE_LIMIT_TO_CLEAR_BYTES = 10_000_000_000L;
    public static String LOG_FILES_PATH = "log";
    public static boolean TEST_PROCESSOR_ACTIVATED = false;


    public static void init() {
        String filePath = "src/main/resources/app-constants.properties";
        updateFromPropertiesFile(filePath);
        log.info("AppConstants List was updated");
    }

    private static void updateFromPropertiesFile(String filePath) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            log.error("File not found or wrong Path");
            e.printStackTrace();
        }

        Class<? extends AppConstants> myClass = AppConstants.class;
        Field[] fields = myClass.getDeclaredFields();

        Arrays.stream(fields).forEach(f -> {
            if (properties.get(f.getName()) != null) {
                try {
                    log.info("Setting value to " + f.getName());
                    f.set(myClass, parseValue(properties.get(f.getName()).toString(), f.getType()));
                    log.info("field name: " + f.getName() + " and new value: " + f.get(myClass));
                } catch (IllegalAccessException e) {
                    log.error("");
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private static Object parseValue(String value, Class<?> type) {
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        } else if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        } else if (type == String.class) {
            return value;
        } else {
            log.error("Unsupported data type: " + type.getName());
            throw new IllegalArgumentException("Unsupported data type: " + type.getName());
        }
    }

}
