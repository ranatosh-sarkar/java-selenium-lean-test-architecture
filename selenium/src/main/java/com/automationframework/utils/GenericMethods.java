package com.automationframework.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.time.LocalDate; 
import java.time.format.DateTimeFormatter; 
import java.util.Locale;

public class GenericMethods {

    private final WebDriver driver;
    private final Actions actions;
    private final WaitHelper waits;
    
 // ISO date used in URL query params (yyyy-MM-dd)
    private static final DateTimeFormatter ISO_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    // Default UI date format used on the site (dd/MM/yyyy)
    private static final DateTimeFormatter UI_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ROOT);

    public GenericMethods(WebDriver driver, WaitHelper waits) {
        this.driver = driver;
        this.actions = new Actions(driver);
        this.waits  = waits;
    }
    
    /** Scrolls the window to the very top (JS). */
    public void scrollToTop() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            // Modern browsers: smooth optional; ignored if unsupported
            js.executeScript("window.scrollTo({ top: 0, behavior: 'instant' });");
        } catch (Exception ignore) {
            // Fallback for older engines
            js.executeScript("window.scrollTo(0, 0);");
        }
    }

    /** Scroll to bottom for symmetry */
    public void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            js.executeScript("window.scrollTo({ top: document.body.scrollHeight, behavior: 'instant' });");
        } catch (Exception ignore) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        }
    }
    
    /** Same token resolver as resolveDateToken, but returns ISO yyyy-MM-dd for URL checks. */
    public String resolveDateTokenIso(String token) {
        if (token == null || token.isEmpty()) return ISO_FMT.format(LocalDate.now());

        String t = token.trim().toLowerCase(Locale.ROOT);
        LocalDate base = LocalDate.now();

        switch (t) {
            case "today":    return ISO_FMT.format(base);
            case "tomorrow": return ISO_FMT.format(base.plusDays(1));
            default:
                if (t.startsWith("plus_")) {
                    int days = parseIntSafe(t.substring("plus_".length()), 0);
                    return ISO_FMT.format(base.plusDays(days));
                }
                if (t.startsWith("minus_")) {
                    int days = parseIntSafe(t.substring("minus_".length()), 0);
                    return ISO_FMT.format(base.minusDays(days));
                }
                // If they passed a literal ISO date already, return it as-is
                return token;
        }
    }

    /* ---------------- Page reads / navigation ---------------- */

    /** Returns current page title (waits until non-empty). */
    public String getTitle() {
        waits.waitForNonEmptyTitle();
        return driver.getTitle();
    }

    /** Returns current URL (no wait needed). */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /** Waits visible and returns the element’s "value" attribute (trimmed). */
    public String readValue(By locator) {
        WebElement el = waits.waitForVisible(locator);
        return el.getAttribute("value").trim();
    }

    /* ---------------- Date token utilities ---------------- */

    /**
     * Resolves tokens to a formatted date string (dd/MM/yyyy):
     *  - "today", "tomorrow"
     *  - "plus_N" (e.g., plus_2, plus_5)
     *  - "minus_N"
     * If token looks like a literal date already, it’s returned as-is.
     */
    public String resolveDateToken(String token) {
        if (token == null || token.isEmpty()) return UI_FMT.format(LocalDate.now());

        String t = token.trim().toLowerCase(Locale.ROOT);
        LocalDate base = LocalDate.now();

        switch (t) {
            case "today":    return UI_FMT.format(base);
            case "tomorrow": return UI_FMT.format(base.plusDays(1));
            default:
                if (t.startsWith("plus_")) {
                    int days = parseIntSafe(t.substring("plus_".length()), 0);
                    return UI_FMT.format(base.plusDays(days));
                }
                if (t.startsWith("minus_")) {
                    int days = parseIntSafe(t.substring("minus_".length()), 0);
                    return UI_FMT.format(base.minusDays(days));
                }
                // literal (assumed already dd/MM/yyyy)
                return token;
        }
    }

    private int parseIntSafe(String s, int fallback) {
        try { return Integer.parseInt(s); } catch (Exception e) { return fallback; }
    }

    /* ---------------- Actions / JS helpers ---------------- */

    public void hover(WebElement element) { actions.moveToElement(element).perform(); }

    public void dragAndDrop(WebElement src, WebElement dest) {
        actions.dragAndDrop(src, dest).perform();
    }
    

    public void jsClick(By locator) {
        WebElement el = waits.waitForVisible(locator);
        jsClick(el);
    }

    public void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
    
    
    private void executeJsClick(WebElement el) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            js.executeScript("arguments[0].click();", el);
        } catch (JavascriptException e) {
            // one soft retry after a short wait (e.g., sticky header/animation)
            waits.smallPause(200);
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", el);
            js.executeScript("arguments[0].click();", el);
        }
    }

    public void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
    
    public void scrollIntoView(By locator) {
        scrollIntoView(waits.waitForVisible(locator));
    }

    public void setValueByJS(WebElement element, String value) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value=arguments[1];", element, value);
    }
}
