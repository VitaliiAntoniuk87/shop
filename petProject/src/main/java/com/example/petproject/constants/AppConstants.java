package com.example.petproject.constants;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        String filePath = "src/main/resources/appconstants.txt";
        Map<String, String> properties = extractPropertiesFromFile(filePath);
        updateAppConstants(properties);
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
        for (String key : keySet) {
            for (Field field : fields) {
                if (key.equals(field.getName())) {
                    try {
                        field.set(myClass, properties.get(key));
                    } catch (IllegalAccessException e) {
                        log.error("Illegal value for constant " + field.getName());
                    }
                }
            }
        }
    }

}
