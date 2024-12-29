package helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ch.qos.logback.classic.Logger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * This class is used to manage a pool of users for testing.
 * It provides methods to acquire and release users to the pool.
 */
public class UserPool {
    // Blocking queue to store available users  
    private static BlockingQueue<JsonObject> availableUsers;
    // Object to synchronize initialization
    private static final Object INIT_LOCK = new Object();
    // Logger instance
    private static final Logger logger = LoggerManager.getLogger(UserPool.class);
    
    // Method to initialize the user pool if it hasn't been initialized yet
    private static void initializeIfNeeded() {
        if (availableUsers == null) {       
            // Synchronize on the initialization lock to ensure thread safety
            synchronized (INIT_LOCK) {
                if (availableUsers == null) {
                    // Initialize the user pool if it hasn't been initialized yet
                    availableUsers = new LinkedBlockingQueue<>();
                    // Get the test data for users
                    JsonArray users = TestDataReader.getTestData("users")
                        .getAsJsonArray("validUsers");
                    // Add each user to the user pool   
                    for (int i = 0; i < users.size(); i++) {
                        availableUsers.offer(users.get(i).getAsJsonObject());
                    }
                    logger.debug("Initialized user pool with {} users", users.size());
                }
            }
        }
    }
    
    // Method to acquire a user from the pool
    public static JsonObject acquireUser() throws InterruptedException {
        initializeIfNeeded();
        logger.debug("Thread {} waiting to acquire user. Available users: {}", 
            Thread.currentThread().getId(), 
            availableUsers.size());
        // Take a user from the pool
        JsonObject user = availableUsers.take();
        logger.debug("Thread {} acquired user: {}. Remaining users: {}", 
            Thread.currentThread().getId(), 
            user.get("email").getAsString(),
            availableUsers.size());
            
        return user;
    }
    
    public static void releaseUser(JsonObject user) {
        initializeIfNeeded();
        availableUsers.offer(user);
        logger.debug("Thread {} released user: {}. Available users: {}", 
            Thread.currentThread().getId(),
         
            user.get("email").getAsString(),
            availableUsers.size());
    }
} 