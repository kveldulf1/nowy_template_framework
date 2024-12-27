package utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestDataManager {
    private static final Logger log = LoggerFactory.getLogger(TestDataManager.class);
    private static final Gson gson = new Gson();
    private static final Map<String, JsonObject> dataCache = new HashMap<>();
    private static final Pattern DYNAMIC_VALUE_PATTERN = Pattern.compile("\\$\\{(.+?)\\}");

    public static JsonObject getTestData(String fileName) {
        return dataCache.computeIfAbsent(fileName, TestDataManager::loadJsonFile);
    }

    private static JsonObject loadJsonFile(String fileName) {
        String filePath = "src/test/resources/testdata/" + fileName + ".json";
        try (FileReader reader = new FileReader(filePath)) {
            log.debug("Loading test data from: {}", filePath);
            return gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            log.error("Failed to load test data file: {}", fileName, e);
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    public static String resolveDynamicValues(String input) {
        if (input == null) return null;

        Matcher matcher = DYNAMIC_VALUE_PATTERN.matcher(input);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String replacement = switch (placeholder) {
                case "timestamp" -> String.valueOf(Instant.now().toEpochMilli());
                default -> placeholder;
            };
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);

        return result.toString();
    }

    public static <T> T getTestData(String fileName, Class<T> classOfT) {
        JsonObject jsonObject = getTestData(fileName);
        String json = gson.toJson(jsonObject);
        json = resolveDynamicValues(json);
        return gson.fromJson(json, classOfT);
    }
} 