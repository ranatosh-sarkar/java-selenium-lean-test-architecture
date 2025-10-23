package com.automationframework.tests.stepdefinitions;

import com.automationframework.core.DriverFactory;
import com.automationframework.utils.LoggerUtil;
import com.automationframework.utils.TestUtils;
import io.cucumber.java.*;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

public class Hooks {

    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        LoggerUtil.info("=== Starting Scenario: " + scenario.getName() + " ===");
    }

    @After(order = 1)
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                byte[] bytes = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Failure Screenshot", new ByteArrayInputStream(bytes));
            } catch (Exception ignored) {}
            TestUtils.captureScreenshot(scenario.getName().replaceAll("\\s+", "_"));
        }
        LoggerUtil.info("=== Finished Scenario: " + scenario.getName() + " (" + scenario.getStatus() + ") ===");
    }
}
