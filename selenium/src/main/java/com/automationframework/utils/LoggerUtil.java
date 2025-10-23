package com.automationframework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {

    // Create a single Log4j2 logger instance for reuse
    private static final Logger log = LogManager.getLogger(LoggerUtil.class);

    private LoggerUtil() {
        // prevent instantiation
    }

    public static void info(String message) {
        log.info(message);
    }

    public static void warn(String message) {
        log.warn(message);
    }

    public static void error(String message) {
        log.error(message);
    }
    
    public static void error(String msg, Throwable t) { 
    	log.error(msg, t); 
    }

    public static void debug(String message) {
        log.debug(message);
    }

    public static void fatal(String message) {
        log.fatal(message);
    }
}
