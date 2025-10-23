package com.automationframework.pages;

import com.automationframework.core.BasePage;
import com.automationframework.utils.LoggerUtil;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    // Locators (merged with actions at page-level)
    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By signInBtn     = By.cssSelector("button[type='submit']");
    private final By errorBanner   = By.cssSelector(".alert-error");

    // Fluent actions
    public LoginPage enterUsername(String username) {
        type(usernameInput, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    public HomePage clickSignIn() {
        click(signInBtn);
        return new HomePage();
    }

    // High-level business operation
    public HomePage loginAs(String username, String password) {
        LoggerUtil.info("Logging in as: " + username);
        return enterUsername(username)
                .enterPassword(password)
                .clickSignIn();
    }

    public String getErrorMessage() { return getText(errorBanner); }
}
