package com.automationframework.pages;

import com.automationframework.core.BasePage;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    private final By profileMenu = By.id("profileMenu");
    private final By logoutLink  = By.id("logout");
    private final By welcomeHdr  = By.cssSelector("h1.welcome");

    public boolean isLoaded() { return isDisplayed(welcomeHdr); }

    public ProfilePage openProfile() {
        click(profileMenu);
        return new ProfilePage();
    }

    public LoginPage logout() {
        click(profileMenu);
        click(logoutLink);
        return new LoginPage();
    }

    public String welcomeText() { return getText(welcomeHdr); }
}
