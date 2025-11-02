package com.automationframework.core;

public abstract class Constants {

    public static final int EXPLICIT_WAIT = 20;
    public static final int IMPLICIT_WAIT = 10;
    public static final int PAGE_LOAD_TIMEOUT = 30;

    public static final String BASE_URL = "https://example.com";
    public static final String REPORT_PATH = System.getProperty("user.dir") + "/reports/";
    public static final String SCREENSHOT_PATH = System.getProperty("user.dir") + "/reports/screenshots/";
}
