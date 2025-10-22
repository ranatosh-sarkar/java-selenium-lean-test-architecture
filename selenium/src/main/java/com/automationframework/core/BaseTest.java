package com.automationframework.core;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.automationframework.enums.BrowserType;
import com.automationframework.utils.LoggerUtil;

public abstract class BaseTest {

    protected WebDriver driver;
    protected ConfigManager config;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
    	LoggerUtil.info("===== Test Setup Started =====");

        // Load environment
        String env = EnvironmentManager.getEnvironmentName();
        config = ConfigManager.getInstance(env);

        // Initialize driver
        String browserName = System.getProperty("browser", "chrome").toUpperCase();
        BrowserType browser = BrowserType.valueOf(browserName);
        DriverFactory.setDriver(browser);
        driver = DriverFactory.getDriver();

        // Navigate to Base URL
        driver.get(config.getProperty("base.url"));
        LoggerUtil.info("🌐 Navigated to: " + config.getProperty("base.url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
    	LoggerUtil.info("===== Test Teardown Started =====");
        DriverFactory.quitDriver();
    }
}
