package helpers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Manages test data loading and processing from JSON files.
 * Provides caching and dynamic value resolution capabilities.
 */
public class TestDataReader {
    private static final Logger logger = new LoggerContext().getLogger(TestDataReader.class);
    
    // Gson instance for JSON processing
    private static final Gson gson = new Gson();
    
    // Cache storing JSON files to avoid repeated disk reads
    private static final Map<String, JsonObject> dataCache = new HashMap<>();
    
    // Regex pattern to find dynamic values like ${timestamp}
    private static final Pattern DYNAMIC_VALUE_PATTERN = Pattern.compile("\\$\\{(.+?)\\}");

    /**
     * Returns JSON data for given filename, using cache if available
     */
    public static JsonObject getTestData(String fileName) {
        return dataCache.computeIfAbsent(fileName, TestDataReader::loadJsonFile);
    }

    /**
     * Loads JSON file from testdata directory
     * Throws RuntimeException if file cannot be loaded
     */
    private static JsonObject loadJsonFile(String fileName) {
        String filePath = "src/test/resources/testdata/" + fileName + ".json";
        try (FileReader reader = new FileReader(filePath)) {
            logger.debug("Loading test data from: {}", filePath);
            return gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            logger.error("Failed to load test data file: {}", fileName, e);
            throw new RuntimeException("Failed to load test data", e);
        }
    }

    /**
     * Replaces dynamic placeholders (${value}) with actual values
     * Currently supports: ${timestamp} - current epoch milliseconds
     */
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

    /**
     * Loads and converts JSON data to specified class type
     * Resolves any dynamic values before conversion
     */
    public static <T> T getTestData(String fileName, Class<T> classOfT) {
        JsonObject jsonObject = getTestData(fileName);
        String json = gson.toJson(jsonObject);
        json = resolveDynamicValues(json);
        return gson.fromJson(json, classOfT);
    }

    /**
     * Returns a random user from the users array in the given JSON file
     */
    public static JsonObject getRandomValidUser() {
        JsonArray users = getTestData("api/requests/login")
            .getAsJsonObject()
            .getAsJsonArray("validUsers");
        return users.get((int) (Math.random() * users.size()))
            .getAsJsonObject();
    }

    public static JsonObject getInvalidUser() {
        JsonArray users = getTestData("api/requests/login")
            .getAsJsonObject()
            .getAsJsonArray("invalidUsers");
        return users.get((int) (Math.random() * users.size()))
            .getAsJsonObject();
    }
}
