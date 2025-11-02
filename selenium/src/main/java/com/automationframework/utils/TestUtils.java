package com.automationframework.utils;

import com.automationframework.core.Constants;
import com.automationframework.core.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestUtils
 *
 * Static utility helpers usable from tests, listeners, and hooks.
 * - timestamp(): generate sortable, unique tokens
 * - captureScreenshot(nameHint): save a screenshot for the current thread's driver
 *
 * Patterns:
 *  - Static Utility (helper methods)
 *  - Service Locator (indirect) via DriverFactory.getDriver()
 */
public class TestUtils {

    /** Returns a compact, sortable timestamp like 20251029_162233_123 */
    public static String timestamp() {
        // Keep time format deterministic
        return new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
    }

    /**
     * Captures a screenshot from the current thread's WebDriver and saves it to
     * reports/screenshots/<nameHint>_<timestamp>.png
     *
     * @return absolute file path string on success; null if driver is null or save failed
     */
    public static String captureScreenshot(String nameHint) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            LoggerUtil.warn("captureScreenshot called with null WebDriver (likely setup failed).");
            return null;
        }

        // Ensure destination directory exists
        Path dir = Path.of(Constants.SCREENSHOT_PATH);
        try { Files.createDirectories(dir); } catch (IOException ignored) {}

        // Take screenshot to a temp file provided by WebDriver
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Build final file path with hint + timestamp
        String fileName = String.format("%s_%s.png", sanitize(nameHint), timestamp());
        Path dest = dir.resolve(fileName);

        try {
            FileUtils.copyFile(src, dest.toFile());
            LoggerUtil.info("Saved screenshot: " + dest.toAbsolutePath());
            return dest.toAbsolutePath().toString();
        } catch (IOException e) {
            LoggerUtil.error("Failed to save screenshot: " + dest, e);
            return null;
        }
    }

    // Prevent illegal chars in file names (basic sanitization)
    private static String sanitize(String s) {
        if (s == null || s.isBlank()) return "screenshot";
        return s.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
