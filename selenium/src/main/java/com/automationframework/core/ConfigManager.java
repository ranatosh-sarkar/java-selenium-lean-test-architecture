package com.automationframework.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.automationframework.utils.LoggerUtil;

public class ConfigManager {

    // (A) Shared configuration store (kept static for minimal impact)
    private static final Properties properties = new Properties();

    // (B) The single instance reference; 'volatile' guarantees visibility
    private static volatile ConfigManager instance = null;
    private static final Object LOCK = new Object();

    // (C) Private constructor prevents 'new' from outside (Singleton ingredient #1)
    private ConfigManager(String env) {
        String configPath = "src/main/resources/config/config-" + env + ".properties";
        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.clear();            // load once at init time
            properties.load(fis);
            LoggerUtil.info("Loaded configuration for environment: " + env);
        } catch (IOException e) {
            throw new RuntimeException(
                "Failed to load configuration for env '" + env + "': " + e.getMessage(), e
            );
        }
    }

    // (D) Global accessor to values (convenience Service-Locator style)
    //     Guard ensures init happened at least once.
    public static String get(String key) {
        if (instance == null) {
            throw new IllegalStateException(
                "Configuration not loaded. Call ConfigManager.getInstance(<env>) first."
            );
        }
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing property key: " + key);
        }
        return value.trim();
    }

    // (E) Thread-safe lazy initialization (Singleton ingredient #2 and #3)
    //     Double-checked locking: first check (no lock), then synchronized block, then second check.
    public static ConfigManager getInstance(String env) {
        if (instance == null) {             // fast path: no locking when already init
            synchronized (LOCK) {
                if (instance == null) {     // only one thread gets here and creates it
                    instance = new ConfigManager(env);
                }
            }
        }
        return instance;
    }

    // nstance-style accessor; reads are safe post-init.
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}

