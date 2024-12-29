package helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ch.qos.logback.classic.Logger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UserPool {
    private static BlockingQueue<JsonObject> availableUsers;
    private static final Object INIT_LOCK = new Object();
    private static final Logger logger = LoggerManager.getLogger(UserPool.class);
    
    private static void initializeIfNeeded() {
        if (availableUsers == null) {
            synchronized (INIT_LOCK) {
                if (availableUsers == null) {
                    availableUsers = new LinkedBlockingQueue<>();
                    JsonArray users = TestDataReader.getTestData("users")
                        .getAsJsonArray("validUsers");
                    
                    for (int i = 0; i < users.size(); i++) {
                        availableUsers.offer(users.get(i).getAsJsonObject());
                    }
                    logger.info("Initialized user pool with {} users", users.size());
                }
            }
        }
    }
    
    public static JsonObject acquireUser() throws InterruptedException {
        initializeIfNeeded();
        logger.info("Thread {} waiting to acquire user. Available users: {}", 
            Thread.currentThread().getId(), 
            availableUsers.size());
            
        JsonObject user = availableUsers.take();
        logger.info("Thread {} acquired user: {}. Remaining users: {}", 
            Thread.currentThread().getId(), 
            user.get("email").getAsString(),
            availableUsers.size());
            
        return user;
    }
    
    public static void releaseUser(JsonObject user) {
        initializeIfNeeded();
        availableUsers.offer(user);
        logger.info("Thread {} released user: {}. Available users: {}", 
            Thread.currentThread().getId(), 
            user.get("email").getAsString(),
            availableUsers.size());
    }
} 