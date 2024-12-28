package helpers;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class LoggerManager {
    public static Logger getLogger(Class<?> clazz) {
        return (Logger) LoggerFactory.getLogger(clazz);
    }
} 