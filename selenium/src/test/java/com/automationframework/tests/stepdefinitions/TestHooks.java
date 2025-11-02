package com.automationframework.tests.stepdefinitions;

import com.automationframework.core.ConfigManager;
import com.automationframework.core.DriverFactory;
import com.automationframework.core.EnvironmentManager;
import com.automationframework.enums.BrowserType;
import com.automationframework.utils.LoggerUtil;
import com.automationframework.utils.TestUtils;
import io.cucumber.java.*;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestHooks {

    /** Runs first: ensure output dirs exist and make allure results dir explicit. */
    @Before(order = -1)
    public void ensureDirs() {
        try {
            String base = System.getProperty("project.basedir", System.getProperty("user.dir"));
            Files.createDirectories(Paths.get(base, "allure-results"));
            Files.createDirectories(Paths.get(base, "reports"));
            Files.createDirectories(Paths.get(base, "logs"));

            // If not already set (e.g., by Surefire), point adapters to ./allure-results
            if (System.getProperty("allure.results.directory") == null) {
                System.setProperty("allure.results.directory", Paths.get(base, "allure-results").toString());
            }
        } catch (Exception ignored) { }
    }

    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        LoggerUtil.info("=== Starting Scenario: " + scenario.getName() + " ===");

        // Load env & config (singleton)
        String env = EnvironmentManager.getEnvironmentName();
        ConfigManager.getInstance(env);

        // Pick browser; be resilient
        BrowserType browser;
        try {
            browser = BrowserType.valueOf(ConfigManager.get("browser").toUpperCase());
        } catch (Exception e) {
            LoggerUtil.warn("Invalid or missing 'browser' in config. Falling back to CHROME.");
            browser = BrowserType.CHROME;
        }

        // Start driver & navigate
        DriverFactory.setDriver(browser);
        String url = ConfigManager.get("base.url");
        DriverFactory.getDriver().get(url);
        LoggerUtil.info("Opened base URL: " + url);
    }

    @After(order = 1)
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                byte[] bytes = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Failure Screenshot", new ByteArrayInputStream(bytes));
            } catch (Exception ignored) { }
            TestUtils.captureScreenshot(scenario.getName().replaceAll("\\s+", "_"));
        }

        LoggerUtil.info("=== Finished Scenario: " + scenario.getName() +
                " (" + scenario.getStatus() + ") ===");
        DriverFactory.quitDriver();
    }
}
