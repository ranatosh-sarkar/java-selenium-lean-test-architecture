package com.automationframework.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;

public class GenericMethods {

    private final WebDriver driver;
    private final Actions actions;

    public GenericMethods(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
    }

    public void hover(WebElement element) { actions.moveToElement(element).perform(); }

    public void dragAndDrop(WebElement src, WebElement dest) {
        actions.dragAndDrop(src, dest).perform();
    }

    public void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void setValueByJS(WebElement element, String value) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", element, value);
    }
}
