package com.automationframework.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

import com.automationframework.enums.BrowserType;
import com.automationframework.utils.LoggerUtil;

/**
 * DriverFactory
 *
 * Responsibility:
 *  - Create a WebDriver instance for a given BrowserType
 *  - Configure baseline timeouts/window state
 *  - Store the driver in a ThreadLocal so each parallel test thread
 *    has its own isolated driver
 *  - Provide accessors to get/quit the thread's driver
 *
 * Patterns:
 *  - Simple Factory (chooses concrete WebDriver based on BrowserType)
 *  - Thread-Local storage (one driver per thread for parallel execution)
 *  - Utility/Static Facade (private ctor + static API)
 */
public final class DriverFactory {

    // Holds a distinct WebDriver per test thread (safe for parallel execution).
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    // Prevent external instantiation (utility/factory class).
    private DriverFactory() {}

    /**
     * Returns the WebDriver bound to the current thread, or null if not set.
     */
    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    /**
     * Creates and wires a WebDriver for the given browser, then binds it to
     * the current thread. Subsequent calls to getDriver() on this thread
     * will return the same instance.
     */
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

        // Baseline browser configuration. Keep implicit waits small; prefer explicit waits.
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.IMPLICIT_WAIT));
        webDriver.manage().window().maximize();

        // Bind this driver instance to the current thread.
        DRIVER.set(webDriver);

        LoggerUtil.info("WebDriver initialized for browser: " + browser);
    }

    /**
     * Quits and removes the WebDriver bound to the current thread, if any.
     * Always call this in teardown to avoid orphaned browser processes.
     */
    public static void quitDriver() {
        WebDriver webDriver = DRIVER.get();
        if (webDriver != null) {
            try {
                webDriver.quit();
                LoggerUtil.info("WebDriver instance closed successfully");
            } finally {
                // Ensure the ThreadLocal is cleared to prevent memory leaks in long-lived runners.
                DRIVER.remove();
            }
        }
    }
}
