package com.automationframework.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WaitHelper {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public WaitHelper(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitForUrlContains(String fragment) {
        return wait.until(ExpectedConditions.urlContains(fragment));
    }

    /** New: wait until page title is non-empty */
    public void waitForNonEmptyTitle() {
        wait.until(d -> !d.getTitle().isEmpty());
    }

    public void smallPause(long millis) { // use sparingly
        try { Thread.sleep(millis); } catch (InterruptedException ignored) {}
    }

    public <T> T withCustomTimeout(Duration timeout, java.util.function.Function<WebDriver, T> condition) {
    	WebDriverWait temp = new WebDriverWait(driver, timeout);
    	return temp.until(condition);
    }
}
