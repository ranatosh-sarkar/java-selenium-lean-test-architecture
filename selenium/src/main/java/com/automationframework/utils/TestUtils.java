package com.automationframework.utils;

import com.automationframework.core.Constants;
import com.automationframework.core.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUtils {

    public static String timestamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
    }

    public static String captureScreenshot(String nameHint) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) return null;

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String path = Constants.SCREENSHOT_PATH + nameHint + "_" + timestamp() + ".png";
        try {
            FileUtils.copyFile(src, new File(path));
            return path;
        } catch (IOException e) {
            LoggerUtil.error("Failed to save screenshot: " + path, e);
            return null;
        }
    }
}
