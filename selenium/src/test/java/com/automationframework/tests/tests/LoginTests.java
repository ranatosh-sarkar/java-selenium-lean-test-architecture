package com.automationframework.tests.tests;

import com.automationframework.core.BaseTest;
import com.automationframework.core.ConfigManager;
import com.automationframework.pages.HomePage;
import com.automationframework.pages.LoginPage;
import com.automationframework.utils.DataProviderUtil;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.automationframework.tests.listeners.TestListener;

@Listeners(TestListener.class)
public class LoginTests extends BaseTest {

    @Test(dataProvider = "excelLoginData", dataProviderClass = DataProviderUtil.class)
    public void login_with_excel_data(String username, String password) {
        HomePage home = new LoginPage().loginAs(username, password);
        Assert.assertTrue(home.isLoaded(), "Home page not loaded");
    }

    @Test
    public void login_from_config() {
        String user = ConfigManager.get("username");
        String pass = ConfigManager.get("password");
        HomePage home = new LoginPage().loginAs(user, pass);
        Assert.assertTrue(home.isLoaded());
    }
}
