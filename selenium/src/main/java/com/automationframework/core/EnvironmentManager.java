package com.automationframework.core;

import com.automationframework.enums.EnvironmentType;

public class EnvironmentManager {

    private static final String DEFAULT_ENV = "qa";

    public static EnvironmentType getEnvironment() {
        String env = System.getProperty("env", DEFAULT_ENV).toLowerCase();

        switch (env) {
            case "dev":
                return EnvironmentType.DEV;
            case "prod":
                return EnvironmentType.PROD;
            default:
                return EnvironmentType.QA;
        }
    }

    public static String getEnvironmentName() {
        return getEnvironment().name().toLowerCase();
    }
}
