package com.automationframework.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.time.Duration;

import com.automationframework.enums.BrowserType;
import com.automationframework.utils.LoggerUtil;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverFactory() {}  // Prevent external instantiation

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(BrowserType browser) {
        WebDriver webDriver;

        switch (browser) {
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                webDriver = new FirefoxDriver();
                break;

            case EDGE:
                WebDriverManager.edgedriver().setup();
                webDriver = new EdgeDriver();
                break;

            case CHROME:
            default:
                WebDriverManager.chromedriver().setup();
                webDriver = new ChromeDriver();
                break;
        }

        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.IMPLICIT_WAIT));
        webDriver.manage().window().maximize();
        driver.set(webDriver);
        LoggerUtil.info("✅ WebDriver initialized for browser: " + browser);
    }

    public static void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            webDriver.quit();
            driver.remove();
            LoggerUtil.info("🧹 WebDriver instance closed successfully");
        }
    }
}
