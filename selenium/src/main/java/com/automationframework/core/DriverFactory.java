package com.automationframework.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

import com.automationframework.enums.BrowserType;
import com.automationframework.utils.LoggerUtil;

public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {}

    private static boolean isCI() {
        // GitHub Actions exposes these; true in CI, false locally
        return "true".equalsIgnoreCase(System.getenv("CI"))
            || "true".equalsIgnoreCase(System.getenv("GITHUB_ACTIONS"));
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void setDriver(BrowserType browser) {
        WebDriver webDriver;

        switch (browser) {
            case FIREFOX: {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opts = new FirefoxOptions();
                if (isCI()) {
                    opts.addArguments("-headless");       // headless in CI
                    opts.addArguments("--width=1920", "--height=1080");
                }
                webDriver = new FirefoxDriver(opts);
                break;
            }

            case EDGE: {
                WebDriverManager.edgedriver().setup();
                EdgeOptions opts = new EdgeOptions();
                if (isCI()) {
                    opts.addArguments("--headless=new", "--no-sandbox",
                            "--disable-dev-shm-usage", "--disable-gpu",
                            "--window-size=1920,1080");
                }
                webDriver = new EdgeDriver(opts);
                break;
            }

            case CHROME:
            default: {
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
                if (isCI()) {
                    // Safe flags for containers/VMs
                    opts.addArguments("--headless=new", "--no-sandbox",
                            "--disable-dev-shm-usage", "--disable-gpu",
                            "--window-size=1920,1080");
                    // If Chrome path is provided by the runner, honor it
                    String chromeBin = System.getenv("CHROME_BIN");
                    if (chromeBin != null && !chromeBin.isBlank()) {
                        opts.setBinary(chromeBin);
                    }
                }
                webDriver = new ChromeDriver(opts);
                break;
            }
        }

        webDriver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(Constants.IMPLICIT_WAIT));
        webDriver.manage().window().maximize();   // headed locally; harmless in CI headless

        DRIVER.set(webDriver);
        LoggerUtil.info("WebDriver initialized for browser: " + browser +
                " (headless=" + isCI() + ")");
    }

    public static void quitDriver() {
        WebDriver webDriver = DRIVER.get();
        if (webDriver != null) {
            try {
                webDriver.quit();
                LoggerUtil.info("WebDriver instance closed successfully");
            } finally {
                DRIVER.remove();
            }
        }
    }
}






//package com.automationframework.core;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.edge.EdgeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//
//import java.time.Duration;
//
//import com.automationframework.enums.BrowserType;
//import com.automationframework.utils.LoggerUtil;
//
///**
// * DriverFactory
// *
// * Responsibility:
// *  - Create a WebDriver instance for a given BrowserType
// *  - Configure baseline timeouts/window state
// *  - Store the driver in a ThreadLocal so each parallel test thread
// *    has its own isolated driver
// *  - Provide accessors to get/quit the thread's driver
// *
// * Patterns:
// *  - Simple Factory (chooses concrete WebDriver based on BrowserType)
// *  - Thread-Local storage (one driver per thread for parallel execution)
// *  - Utility/Static Facade (private ctor + static API)
// */
//public final class DriverFactory {
//
//    // Holds a distinct WebDriver per test thread (safe for parallel execution).
//    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
//
//    // Prevent external instantiation (utility/factory class).
//    private DriverFactory() {}
//
//    /**
//     * Returns the WebDriver bound to the current thread, or null if not set.
//     */
//    public static WebDriver getDriver() {
//        return DRIVER.get();
//    }
//
//    /**
//     * Creates and wires a WebDriver for the given browser, then binds it to
//     * the current thread. Subsequent calls to getDriver() on this thread
//     * will return the same instance.
//     */
//    public static void setDriver(BrowserType browser) {
//        WebDriver webDriver;
//
//        switch (browser) {
//            case FIREFOX:
//                WebDriverManager.firefoxdriver().setup();
//                webDriver = new FirefoxDriver();
//                break;
//
//            case EDGE:
//                WebDriverManager.edgedriver().setup();
//                webDriver = new EdgeDriver();
//                break;
//
//            case CHROME:
//            default:
//                WebDriverManager.chromedriver().setup();
//                webDriver = new ChromeDriver();
//                break;
//        }
//
//        // Baseline browser configuration. Keep implicit waits small; prefer explicit waits.
//        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.IMPLICIT_WAIT));
//        webDriver.manage().window().maximize();
//
//        // Bind this driver instance to the current thread.
//        DRIVER.set(webDriver);
//
//        LoggerUtil.info("WebDriver initialized for browser: " + browser);
//    }
//
//    /**
//     * Quits and removes the WebDriver bound to the current thread, if any.
//     * Always call this in teardown to avoid orphaned browser processes.
//     */
//    public static void quitDriver() {
//        WebDriver webDriver = DRIVER.get();
//        if (webDriver != null) {
//            try {
//                webDriver.quit();
//                LoggerUtil.info("WebDriver instance closed successfully");
//            } finally {
//                // Ensure the ThreadLocal is cleared to prevent memory leaks in long-lived runners.
//                DRIVER.remove();
//            }
//        }
//    }
//}
