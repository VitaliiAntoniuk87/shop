package com.example.petproject.constants;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Log4j2
@AllArgsConstructor
public class AppConstants {

    public static final boolean CART_CLEAN_UP_PROCESSOR_ACTIVATED = true;
    public static final long CART_AUTO_CANCELLATION_TIMEOUT_MINUTES = 2880;
    public static final boolean LOGS_CLEAN_UP_PROCESSOR_ACTIVATED = false;
    public static final long LOGS_CLEAN_UP_PROCESSOR_TIMEOUT_MINUTES = 1440;
    public static final long LOG_FILE_SIZE_LIMIT_BYTES = 10_000_000_000L;
    public static final String LOG_FILES_PATH = "/log";
    public static final boolean TEST_PROCESSOR_ACTIVATED = true;

    public static void init() {
        String filePath = "src/main/resources/appconstants.properties";
        updateAppConstants(extractPropertiesFromFile(filePath));
    }

    private static Map<String, String> extractPropertiesFromFile(String filePath) {
        HashMap<String, String> properties = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Pattern pattern = Pattern.compile("^(.*?)=(.*?)$");

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);

                if (matcher.matches()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);
                    properties.put(key.trim(), value.trim());
                }
            }
        } catch (IOException e) {
            log.error("Error while reading the file");
        }
        return properties;
    }

    private static void updateAppConstants(Map<String, String> properties) {
        Class<? extends AppConstants> myClass = AppConstants.class;
        Set<String> keySet = properties.keySet();
        Field[] fields = myClass.getDeclaredFields();
        for (Field field : fields) {
            for (String key : keySet) {
                if (field.getName().equals(key)) {
                    try {
                        field.set(myClass, parseValue(properties.get(key), field.getType()));
                    } catch (IllegalAccessException e) {
                        log.error("Illegal value for constant " + field.getName());
                    }
                }
            }
        }
    }

    private static Object parseValue(String value, Class<?> type) {
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        } else if (type == int.class || type == Integer.class) {
            return Long.parseLong(value);
        } else if (type == String.class) {
            return value;
        } else {
            log.error("Unsupported data type: " + type.getName());
            throw new IllegalArgumentException("Unsupported data type: " + type.getName());
        }
    }

}
