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
        return "true".equalsIgnoreCase(System.getenv("CI"))
            || "true".equalsIgnoreCase(System.getenv("GITHUB_ACTIONS"));
    }

    public static WebDriver getDriver() { return DRIVER.get(); }

    public static void setDriver(BrowserType browser) {
        WebDriver webDriver;

        switch (browser) {
            case FIREFOX: {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions opts = new FirefoxOptions();
                if (isCI()) {
                    // Headless and desktop viewport in CI
                    opts.addArguments("-headless");               // firefox flag
                    opts.addArguments("--width=1920", "--height=1080");
                }
                webDriver = new FirefoxDriver(opts);
                break;
            }

            case EDGE: {
                WebDriverManager.edgedriver().setup();
                EdgeOptions opts = new EdgeOptions();
                if (isCI()) {
                    // Chromium-safe flags for CI/containers
                    opts.addArguments(
                        "--headless=new",
                        "--window-size=1920,1080",
                        "--disable-gpu",
                        "--no-sandbox",
                        "--disable-dev-shm-usage"
                    );
                }
                webDriver = new EdgeDriver(opts);
                break;
            }

            case CHROME:
            default: {
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();

                if (isCI()) {
                    opts.addArguments(
                        "--headless=new",
                        "--window-size=1920,1080",
                        "--disable-gpu",
                        "--no-sandbox",
                        "--disable-dev-shm-usage"
                    );
                    // GitHub runner exposes Chrome path
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

        // In headed local runs, maximize; in CI headless the window-size already applies.
        try { webDriver.manage().window().maximize(); } catch (Exception ignored) {}

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
