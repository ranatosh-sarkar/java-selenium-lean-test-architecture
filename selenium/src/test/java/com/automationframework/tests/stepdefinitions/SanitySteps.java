package com.automationframework.tests.stepdefinitions;

import com.automationframework.core.ConfigManager;
import com.automationframework.core.DriverFactory;
import com.automationframework.core.EnvironmentManager;
import com.automationframework.enums.BrowserType;      // <-- add this import
import com.automationframework.utils.LoggerUtil;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class SanitySteps {

    private WebDriver driver;

    @Before(order = 0)
    public void init() {
        // 1) Load env + config using your current core APIs
        String env = EnvironmentManager.getEnvironmentName(); // "qa" | "dev" | "prod"
        ConfigManager.getInstance(env);

        // 2) Resolve browser -> BrowserType enum safely
        String browserSys = System.getProperty("browser", "chrome").toUpperCase();
        BrowserType browser;
        try {
            browser = BrowserType.valueOf(browserSys);
        } catch (IllegalArgumentException ex) {
            browser = BrowserType.CHROME; // safe default
        }

        // 3) Initialize driver via your new API (same as BaseTest)
        DriverFactory.setDriver(browser);
        driver = DriverFactory.getDriver();

        LoggerUtil.info("[Sanity] Initialized driver for env=" + env + ", browser=" + browser);
    }

    @Given("I open the application")
    public void i_open_the_application() {
        String url = ConfigManager.get("base.url");   // keep this
        driver.get(url);
        LoggerUtil.info("[Sanity] Navigated to: " + url);
    }

    @Then("the page title should match config")
    public void the_page_title_should_match_config() {
        String expected = ConfigManager.get("app.title");  // keep this
        String actual = driver.getTitle();
        LoggerUtil.info("[Sanity] Title -> expected='" + expected + "', actual='" + actual + "'");
        Assert.assertEquals(actual, expected, "Page title mismatch");
    }
}
