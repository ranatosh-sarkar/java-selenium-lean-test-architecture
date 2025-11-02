package com.automationframework.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.automationframework.core.BasePage;
import com.automationframework.utils.LoggerUtil;

public class SanityTestPageObject extends BasePage {
	
	// ---------- LOCATORS ----------
    private static final By FIRST_NAME = By.xpath("//*[@id='root-container']/div/div[2]/div/div[2]/div/div/form/div[1]/input");
    private static final By LAST_NAME = By.xpath("//*[@id='root-container']/div/div[2]/div/div[2]/div/div/form/div[2]/input");
    private static final By EMAIL  = By.xpath("//*[@id='root-container']/div/div[2]/div/div[2]/div/div/form/div[3]/input");
    private static final By PHONE = By.xpath("//*[@id='root-container']/div/div[2]/div/div[2]/div/div/form/div[4]/input");
    
    private static final By BTN_RESERVE_NOW = By.xpath("//*[@id='root-container']/div/div[2]/div/div[2]/div/div/form/button[1]");
    private static final By BOOKING_CONFIRMED_TEXT = By.xpath("//*[@id='root-container']/div/div[2]/div/div[2]/div/div/h2");
	
    public SanityTestPageObject enterUserDetails(String firstName, String lastName, String email, String phone) throws InterruptedException {
        LoggerUtil.info(String.format("[DETAILS] first='%s', last='%s', email='%s', phone='%s'",
                firstName, lastName, email, phone));

        // BasePage#type() already waits -> clear -> sendKeys -> logs
        type(FIRST_NAME, firstName);
        type(LAST_NAME,  lastName);
        type(EMAIL,      email);
        type(PHONE,      phone);
        
        return this;
    }

    /** Click the “Reserve Now” button (uses JS click helper from BasePage). */
    public SanityTestPageObject clickReserveNow() {
        // Prefer standard click; fallback to JS if needed
        try {
            click(BTN_RESERVE_NOW);
        } catch (Exception e) {
            LoggerUtil.warn("Normal click failed, retrying with JS click for Reserve Now");
            jsClick(BTN_RESERVE_NOW);
        }
        LoggerUtil.info("[CLICK] Reserve Now");
        return this;
    }

    /** Verify the confirmation heading text. */
    public SanityTestPageObject verifyBookingConfirmed(String expectedText) {
    	
    	waits.smallPause(2000);
        // Wait until the confirmation element is visible
        waits.withCustomTimeout(Duration.ofSeconds(5),
            d -> ExpectedConditions.visibilityOfElementLocated(BOOKING_CONFIRMED_TEXT).apply(d)
        );

        // Fetch actual text
        String actual = getText(BOOKING_CONFIRMED_TEXT).trim();
        LoggerUtil.info("Visibility [CONFIRMED] expected='" + expectedText + "', actual='" + actual + "'");

        // Log the comparison
        LoggerUtil.info("[CONFIRM] expected='" + expectedText + "', actual='" + actual + "'");

        // Assert match
        Assert.assertEquals(
            actual.toLowerCase(),
            expectedText.trim().toLowerCase(),
            "Booking confirmation text mismatch"
        );

        return this;
    }

}
