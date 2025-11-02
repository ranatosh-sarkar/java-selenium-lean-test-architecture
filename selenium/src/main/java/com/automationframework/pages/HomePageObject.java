package com.automationframework.pages;

import com.automationframework.core.BasePage;
import com.automationframework.utils.LoggerUtil;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

/**
 * HomePageObject
 *
 * Page Object for the Shady Meadows home page. Encapsulates:
 *  - Locators for headers, footers, buttons, date inputs
 *  - Business actions (set booking dates, click key buttons)
 *  - Verifications (welcome text, headers/footers, title, reservation URL)
 *
 * Patterns: Page Object (encapsulation), Fluent interface (return this),
 * Data mapping (key->By maps), Facade via BasePage helpers.
 */
public class HomePageObject extends BasePage {

    // ---------- BUTTON/KEY ELEMENT LOCATORS ----------
    private static final By WELCOME_TEXT_HOMEPAGE = By.xpath("//*[@id='root-container']/div/section[1]/div/div/div/h1");
    private static final By BTN_BOOK_NOW           = By.xpath("//*[@id='root-container']/div/section[1]/div/div/div/a");
    private static final By BTN_CHECK_AVAILABILITY = By.xpath("//*[@id='booking']/div/div/div/form/div/div[4]/button");
    private static final By BTN_SINGLE_BOOK_NOW    = By.xpath("//*[@id='rooms']/div/div[2]/div[1]/div/div[3]/a");
    private static final By BTN_DOUBLE_BOOK_NOW    = By.xpath("//*[@id='rooms']/div/div[2]/div[2]/div/div[3]/a");
    private static final By BTN_SUITE_BOOK_NOW     = By.xpath("//*[@id='rooms']/div/div[2]/div[3]/div/div[3]/a");
    private static final By BTN_FORM_SUBMIT        = By.xpath("//*[@id='contact']/div/div/div/div/div/form/div[6]/button");
    private static final By BTN_RESERVE_NOW        = By.xpath("//*[@id='doReservation']");

    // ---------- HEADER LOCATORS ----------
    private static final By HEADER_MAIN    = By.xpath("//*[@id='root-container']/div/nav/div/a/span");
    private static final By HEADER_ROOMS   = By.xpath("//*[@id='navbarNav']/ul/li[1]/a");
    private static final By HEADER_BOOK    = By.xpath("//*[@id='navbarNav']/ul/li[2]/a");
    private static final By HEADER_AMEN    = By.xpath("//*[@id='navbarNav']/ul/li[3]/a");
    private static final By HEADER_LOC     = By.xpath("//*[@id='navbarNav']/ul/li[4]/a");
    private static final By HEADER_CONTACT = By.xpath("//*[@id='navbarNav']/ul/li[5]/a");
    private static final By HEADER_ADMIN   = By.xpath("//*[@id='navbarNav']/ul/li[6]/a");

    // ---------- FOOTER LOCATORS ----------
    private static final By FOOTER_BRAND   = By.xpath("//*[@id='root-container']/div/footer/div/div[1]/div[1]/h5");
    private static final By FOOTER_CONTACT = By.xpath("//*[@id='root-container']/div/footer/div/div[1]/div[2]/h5");
    private static final By FOOTER_LINKS   = By.xpath("//*[@id='root-container']/div/footer/div/div[1]/div[3]/h5");

    // ---------- FORM/SCROLL TARGETS ----------
    private static final By SCROLL_TOVIEW_CHECKIN_SECTION   = By.xpath("//*[@id='root-container']/div/section[1]/div/div/div/a");
    private static final By SCROLL_TOVIEW_ROOMS_SECTION     = By.xpath("//*[@id='rooms']/div/div[1]/h2");
    private static final By SCROLL_TOVIEW_ROOM_DESCRIPTION_SECTION = By.xpath("//*[@id='root-container']/div/div[2]/div/div[1]/div[3]/h2");
    private static final By CHECK_IN_INPUT   = By.xpath("//*[@id='booking']/div/div/div/form/div/div[1]/div/div/input");
    private static final By CHECK_OUT_INPUT  = By.xpath("//*[@id='booking']/div/div/div/form/div/div[2]/div/div/input");
    private static final By ROOM_H1          = By.xpath("//*[@id='root-container']/div/div[2]/div/div[1]/div[1]/h1");

    /**
     * Sets the booking dates using natural-language tokens (e.g., "today", "tomorrow", "plus_2").
     * Uses BasePage scroll + GenericMethods for date token resolution.
     */
    public HomePageObject setBookingDates(String checkInToken, String checkOutToken) throws InterruptedException {
        scrollIntoView(SCROLL_TOVIEW_CHECKIN_SECTION);
        Thread.sleep(2000); // TODO: replace with an explicit wait for the section/input to be interactable

        String checkInUi  = actions.resolveDateToken(checkInToken);   // dd/MM/yyyy (UI format)
        String checkOutUi = actions.resolveDateToken(checkOutToken);

        clearTypeTabWindows(CHECK_IN_INPUT,  checkInUi);
        Thread.sleep(2000); // TODO: replace with explicit wait
        clearTypeTabWindows(CHECK_OUT_INPUT, checkOutUi);

        LoggerUtil.info("[SET DATES] checkIn=" + checkInUi + "  checkOut=" + checkOutUi);
        return this;
    }

    /** Cross-platform type/clear; special handling for Windows date inputs. */
    private void clearTypeTabWindows(By locator, String value) {
        WebElement el = waits.waitForVisible(locator);
        el.click();

        if (isWindows()) {
            el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            el.sendKeys(Keys.BACK_SPACE);
            el.sendKeys(value);
            el.sendKeys(Keys.TAB);
        } else {
            try { el.clear(); } catch (Exception ignored) {}
            el.sendKeys(value);
            el.sendKeys(Keys.TAB);
        }
    }

    private boolean isWindows() {
        return System.getProperty("os.name", "").toLowerCase().contains("win");
    }

    /**
     * Clicks buttons using a human-readable label (from Gherkin).
     * Delegates to BasePage/GenericMethods; includes minimal scrolling.
     */
    public HomePageObject clickButtonByLabel(String label) throws InterruptedException {
        String key = label.trim().toLowerCase();
        switch (key) {
            case "check availability":
                scrollIntoView(BTN_CHECK_AVAILABILITY);
                click(BTN_CHECK_AVAILABILITY);
                break;
            case "room - book now":
                scrollIntoView(SCROLL_TOVIEW_ROOMS_SECTION);
                Thread.sleep(2000); // TODO: explicit wait for the card/button
                actions.jsClick(BTN_SINGLE_BOOK_NOW);
                Thread.sleep(1000);
                break;
            case "reserve now":
                scrollIntoView(SCROLL_TOVIEW_ROOM_DESCRIPTION_SECTION);
                Thread.sleep(2000);
                actions.jsClick(BTN_RESERVE_NOW);
                Thread.sleep(1000);
                actions.scrollToTop();
                break;
            default:
                throw new IllegalArgumentException("Unsupported button label: " + label);
        }
        LoggerUtil.info("[CLICK] " + label);
        return this;
    }

    /** Returns the room header (for assertions when on the Single/Double/Suite page). */
    public String singleRoomHeader() {
        scrollIntoView(ROOM_H1);
        return getText(ROOM_H1).trim();
    }

    /**
     * Validates reservation URL has expected path and ISO yyyy-MM-dd query params
     * derived from date tokens.
     */
    public HomePageObject verifyReservationUrl(String checkInToken, String checkOutToken, String fragmentToken) {
        String url = currentUrl();

        String checkInIso  = toIso(checkInToken);
        String checkOutIso = toIso(checkOutToken);
        String fragIso     = toIso(fragmentToken);

        LoggerUtil.info("[VERIFY URL] url=" + url +
                " | checkin=" + checkInIso + " | checkout=" + checkOutIso + " | frag=" + fragIso);

        Assert.assertTrue(url.contains("/reservation/"),       "Reservation path missing. URL=" + url);
        Assert.assertTrue(url.contains("checkin=" + checkInIso),"Missing/incorrect checkin in URL. URL=" + url);
        Assert.assertTrue(url.contains("checkout=" + checkOutIso),"Missing/incorrect checkout in URL. URL=" + url);
        Assert.assertTrue(url.contains(fragIso),                "Expected fragment ISO date not found: " + fragIso);

        return this;
    }

    /** Validates the page's H1 equals any of the expected room names. */
    public HomePageObject verifyRoomPageTitle(String single, String doubleRoom, String suite) {
        String actual = getText(ROOM_H1).trim();
        boolean ok = actual.equalsIgnoreCase(single)
                  || actual.equalsIgnoreCase(doubleRoom)
                  || actual.equalsIgnoreCase(suite);

        if (!ok) {
            Assert.fail("Room header mismatch. Expected one of ['" + single + "', '" + doubleRoom + "', '" + suite
                    + "'], Actual='" + actual + "'");
        }
        return this;
    }

    // ---------- small helpers ----------
    private String toIso(String token) {
        return actions.resolveDateTokenIso(token);
    }

    /** Asserts default check-in/out inputs match expected token-derived values (UI format). */
    public HomePageObject verify_default_dates(Map<String, String> expected) {
        scrollIntoView(CHECK_IN_INPUT);
        scrollIntoView(CHECK_OUT_INPUT);

        String actualIn  = actions.readValue(CHECK_IN_INPUT);
        String actualOut = actions.readValue(CHECK_OUT_INPUT);

        String expIn  = actions.resolveDateToken(expected.getOrDefault("check_in",  "today"));
        String expOut = actions.resolveDateToken(expected.getOrDefault("check_out", "tomorrow"));

        LoggerUtil.info("[DEFAULT DATES] expectedIn=" + expIn + ", actualIn=" + actualIn +
                        " | expectedOut=" + expOut + ", actualOut=" + actualOut);

        Assert.assertEquals(actualIn,  expIn,  "Check-in date mismatch");
        Assert.assertEquals(actualOut, expOut, "Check-out date mismatch");
        return this;
    }

    // ---------- DataTable key -> locator maps ----------
    private static final Map<String, By> HEADER = new HashMap<>();
    private static final Map<String, By> FOOTER = new HashMap<>();
    static {
        HEADER.put("header_main", HEADER_MAIN);
        HEADER.put("rooms",       HEADER_ROOMS);
        HEADER.put("booking",     HEADER_BOOK);
        HEADER.put("amenities",   HEADER_AMEN);
        HEADER.put("location",    HEADER_LOC);
        HEADER.put("contact",     HEADER_CONTACT);
        HEADER.put("admin",       HEADER_ADMIN);

        FOOTER.put("footer_brand", FOOTER_BRAND);
        FOOTER.put("contact_us",   FOOTER_CONTACT);
        FOOTER.put("quick_links",  FOOTER_LINKS);
    }

    /** Header verification using DataTable keys. */
    public HomePageObject verify_homePage_headers(Map<String, String> expectedMap) {
        for (Map.Entry<String, String> e : expectedMap.entrySet()) {
            String key = e.getKey();
            String expected = e.getValue();

            By locator = HEADER.get(key);
            Assert.assertNotNull(locator, "Unknown header key: " + key);

            scrollIntoView(locator);
            String actual = getText(locator).trim();
            boolean displayed = isDisplayed(locator);

            LoggerUtil.info(String.format(
                "[HEADER CHECK] key=%s expected='%s' actual='%s' displayed=%s",
                key, expected, actual, displayed));

            Assert.assertTrue(displayed, "Header item not displayed for key: " + key);
            Assert.assertEquals(
                normalizeText(actual).toLowerCase(java.util.Locale.ROOT),
                normalizeText(expected).toLowerCase(java.util.Locale.ROOT),
                "Header label mismatch for key: " + key
            );
        }
        return this;
    }

    /** Footer verification using DataTable keys. */
    public HomePageObject verify_homePage_footers(Map<String, String> expectedMap) {
        for (Map.Entry<String, String> e : expectedMap.entrySet()) {
            String key = e.getKey();
            String expected = e.getValue();

            By locator = FOOTER.get(key);
            Assert.assertNotNull(locator, "Unknown footer key: " + key);

            scrollIntoView(locator);
            String actual = getText(locator).trim();
            boolean displayed = isDisplayed(locator);

            LoggerUtil.info(String.format(
                "[FOOTER CHECK] key=%s expected='%s' actual='%s' displayed=%s",
                key, expected, actual, displayed));

            Assert.assertTrue(displayed, "Footer item not displayed for key: " + key);
            Assert.assertEquals(
                normalizeText(actual).toLowerCase(java.util.Locale.ROOT),
                normalizeText(expected).toLowerCase(java.util.Locale.ROOT),
                "Footer label mismatch for key: " + key
            );
        }
        return this;
    }

    /** Button label verification using DataTable keys. */
    public HomePageObject verify_homePage_allButtonElements(Map<String, String> expectedLabelsByKey) {
        for (Map.Entry<String, String> entry : expectedLabelsByKey.entrySet()) {
            String key = entry.getKey();
            String expectedLabel = entry.getValue();

            By locator = BUTTONS.get(key);
            Assert.assertNotNull(locator, "Unknown button key: " + key);

            scrollIntoView(locator);
            String actual = getText(locator).trim();
            boolean displayed = isDisplayed(locator);

            LoggerUtil.info(String.format(
                    "[BUTTON CHECK] key=%s expected='%s' actual='%s' displayed=%s",
                    key, expectedLabel, actual, displayed));

            Assert.assertTrue(displayed, "Button not displayed for key: " + key);
            Assert.assertEquals(
                    normalizeText(actual).toLowerCase(java.util.Locale.ROOT),
                    normalizeText(expectedLabel).toLowerCase(java.util.Locale.ROOT),
                    "Label mismatch for key: " + key
            );
        }
        return this;
    }

    private String normalizeText(String s) {
        return s == null ? "" : s.replaceAll("\\s+", " ").trim();
    }

    // Key -> Locator (used for DataTable-driven UI checks)
    private static final Map<String, By> BUTTONS = new HashMap<>();
    static {
        BUTTONS.put("btn_book_now",            BTN_BOOK_NOW);
        BUTTONS.put("btn_check_availability",  BTN_CHECK_AVAILABILITY);
        BUTTONS.put("btn_single_book_now",     BTN_SINGLE_BOOK_NOW);
        BUTTONS.put("btn_double_book_now",     BTN_DOUBLE_BOOK_NOW);
        BUTTONS.put("btn_suite_book_now",      BTN_SUITE_BOOK_NOW);
        BUTTONS.put("btn_form_submit",         BTN_FORM_SUBMIT);
    }

    /** Home page welcome text check. */
    public HomePageObject verify_homePage_welcomeText(String expectedWelcomeText) {
        String actual = getText(WELCOME_TEXT_HOMEPAGE).trim();
        LoggerUtil.info("[WELCOME TEXT CHECK] expected='" + expectedWelcomeText + "' | actual='" + actual + "'");
        Assert.assertEquals(actual, expectedWelcomeText,
                "Welcome text mismatch: expected='" + expectedWelcomeText + "', actual='" + actual + "'");
        return this;
    }

    /** Title & URL wrappers (via GenericMethods in BasePage). */
    public String title()       { return actions.getTitle(); }
    public String currentUrl()  { return actions.getCurrentUrl(); }

    public HomePageObject assertTitleIs(String expectedPageTitle) {
    	
    	waits.withCustomTimeout(Duration.ofSeconds(2),
    	d -> ExpectedConditions.titleIs(expectedPageTitle).apply(d));
    	
        String actual = title();
        LoggerUtil.info("[TITLE CHECK] expected='" + expectedPageTitle + "' | actual='" + actual + "'");
        Assert.assertEquals(actual, expectedPageTitle,
                "Title mismatch: expected='" + expectedPageTitle + "', actual='" + actual + "'");
        return this;
    }

    /** Compares base URL ignoring a trailing '/'. */
    public boolean matchesBaseUrl(String expectedBase) {
        String exp = normalizeUrl(expectedBase);
        String act = normalizeUrl(currentUrl());
        return act.startsWith(exp);
    }

    private String normalizeUrl(String url) {
        if (url == null) return "";
        String u = url.trim();
        if (u.endsWith("/")) u = u.substring(0, u.length() - 1);
        return u;
    }
}
