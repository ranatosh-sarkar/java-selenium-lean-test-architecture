package com.automationframework.tests.stepdefinitions;

import com.automationframework.core.BaseTest;
import com.automationframework.core.ConfigManager;
import com.automationframework.pages.HomePage;
import com.automationframework.pages.LoginPage;
import org.testng.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class LoginSteps extends BaseTest {

    private LoginPage login;
    private HomePage home;

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        // BaseTest already navigates to base.url
        login = new LoginPage();
    }

    @When("I login with {string} and {string}")
    public void i_login_with_and(String user, String pass) {
        // if placeholders used in examples, you can resolve from config
        if ("${username}".equals(user))  user = ConfigManager.get("username");
        if ("${password}".equals(pass))  pass = ConfigManager.get("password");

        home = login.loginAs(user, pass);
    }

    @Then("I should land on the home page")
    public void i_should_land_on_the_home_page() {
        Assert.assertTrue(home.isLoaded(), "Home page did not load");
    }
}
