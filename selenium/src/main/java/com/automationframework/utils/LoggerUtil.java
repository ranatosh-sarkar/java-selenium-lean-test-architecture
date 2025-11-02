package com.automationframework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoggerUtil {

    private static final Logger log = LogManager.getLogger(LoggerUtil.class);

    static {
        // Make sure logs/ exists (helps on locked-down environments)
        try {
            Path dir = Paths.get(System.getProperty("user.dir"), "logs");
            Files.createDirectories(dir);
        } catch (Exception ignored) {
            // If creation fails, Log4j2 will still try; worst case it logs to console only
        }
    }

    private LoggerUtil() {}

    public static void info(String message)  { log.info(message); }
    public static void warn(String message)  { log.warn(message); }
    public static void error(String message) { log.error(message); }
    public static void error(String msg, Throwable t) { log.error(msg, t); }
    public static void debug(String message) { log.debug(message); }
    public static void fatal(String message) { log.fatal(message); }
}
