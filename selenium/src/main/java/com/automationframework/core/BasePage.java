package com.automationframework.core;

import com.automationframework.utils.WaitHelper;
import com.automationframework.utils.GenericMethods;
import com.automationframework.utils.LoggerUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final WaitHelper waits;
    protected final GenericMethods actions;

    protected BasePage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.EXPLICIT_WAIT));
        this.waits = new WaitHelper(driver, wait);
        this.actions = new GenericMethods(driver);
    }

    // ---------- Common low-level wrappers (delegates to helpers) ----------
    protected WebElement find(By locator) { return driver.findElement(locator); }

    protected void click(By locator) {
        waits.waitForClickable(locator);
        find(locator).click();
        LoggerUtil.info("Clicked: " + locator.toString());
    }

    protected void type(By locator, String text) {
        waits.waitForVisible(locator);
        WebElement el = find(locator);
        el.clear();
        el.sendKeys(text);
        LoggerUtil.info("Typed into " + locator.toString() + " => " + text);
    }

    protected String getText(By locator) {
        waits.waitForVisible(locator);
        String val = find(locator).getText();
        LoggerUtil.info("Text from " + locator.toString() + " => " + val);
        return val;
    }

    protected boolean isDisplayed(By locator) {
        try {
            return find(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected void jsClick(By locator) { actions.jsClick(find(locator)); }
    protected void scrollIntoView(By locator) { actions.scrollIntoView(find(locator)); }

    // ---------- Navigation helpers ----------
    public String getPageTitle() { return driver.getTitle(); }
    public String getCurrentUrl() { return driver.getCurrentUrl(); }
}
