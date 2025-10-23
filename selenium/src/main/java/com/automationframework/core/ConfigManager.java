package com.automationframework.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.automationframework.utils.LoggerUtil;

public class ConfigManager {

    private static Properties properties = new Properties();
    private static ConfigManager instance = null;

    private ConfigManager(String env) {
        try {
            String configPath = "src/main/resources/config/config-" + env + ".properties";
            FileInputStream fis = new FileInputStream(configPath);
            properties.load(fis);
            LoggerUtil.info("Loaded configuration for environment: " + env);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration: " + e.getMessage());
        }
    }
    
    public static String get(String key) {
        if (properties == null) {
            throw new IllegalStateException("Configuration not loaded. Call ConfigManager.loadConfig(env) first.");
        }
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing property key: " + key);
        }
        return value.trim();
    }

    public static ConfigManager getInstance(String env) {
        if (instance == null)
            instance = new ConfigManager(env);
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
