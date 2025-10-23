package com.automationframework.pages;

import com.automationframework.core.BasePage;
import org.openqa.selenium.By;

public class ProfilePage extends BasePage {
    private final By fullName = By.id("fullName");
    private final By email    = By.id("email");

    public String getFullName() { return getText(fullName); }
    public String getEmail()    { return getText(email); }
}
