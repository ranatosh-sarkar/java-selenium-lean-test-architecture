package com.automationframework.tests.stepdefinitions;

import java.util.Map;

import org.testng.Assert;

import com.automationframework.core.ConfigManager;
import com.automationframework.pages.HomePageObject;
import com.automationframework.utils.LoggerUtil;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class HomePageStepDefination {

	private final HomePageObject homePageObject = new HomePageObject();
	
    private String lastCheckInToken;
    private String lastCheckOutToken;
    
    @Given("I am on the home page")
    public void i_am_on_the_home_page() throws InterruptedException {
        LoggerUtil.info("State: On home page");

        String expected = ConfigManager.get("base.url");
        String actual   = homePageObject.currentUrl();

        LoggerUtil.info("[URL CHECK] expectedBase=" + expected + " | actual=" + actual);
        boolean ok = homePageObject.matchesBaseUrl(expected); 
        Assert.assertTrue(ok, "URL mismatch. Expected base: " + expected + " | Actual: " + actual);
    }

    @Then("the page title should be {string}")
    public void the_page_title_should_be(String expectedPageTitle) throws InterruptedException {
    	
//        String actualTitle = homePageObject.title(); 
//        LoggerUtil.info("[TITLE CHECK] expected='" + expectedPageTitle + "' | actual='" + actualTitle + "'");
//        Assert.assertEquals(actualTitle, expectedPageTitle,
//                "Title mismatch: expected='" + expectedPageTitle + "', actual='" + actualTitle + "'");
    	
         homePageObject.assertTitleIs("Restful-booker-platform demo");
    	
    }

    @Then("the welcome text should be {string}")
    public void the_welcome_text_should_be(String expectedWelcomeText) {
    	homePageObject.verify_homePage_welcomeText(expectedWelcomeText);
    }

    @Then("the buttons should be present:")
    public void the_buttons_should_be_present(DataTable dataTable) {
        Map<String, String> expected = dataTable.asMap(String.class, String.class);
        homePageObject.verify_homePage_allButtonElements(expected);
    }

    @Then("the header should show:")
    public void the_header_should_show(DataTable dataTable) {
        Map<String, String> expected = dataTable.asMap(String.class, String.class);
        homePageObject.verify_homePage_headers(expected);
    }

    @Then("the footer should show:")
    public void the_footer_should_show(DataTable dataTable) {
        Map<String, String> expected = dataTable.asMap(String.class, String.class);
        homePageObject.verify_homePage_footers(expected);
    }

    @Then("the default dates should be:")
    public void the_default_dates_should_be(io.cucumber.datatable.DataTable dataTable) {
    	Map<String, String> expected = dataTable.asMap(String.class, String.class);
        new HomePageObject().verify_default_dates(expected);
    }

    @Then("I set the dates:")
    public void i_set_the_dates(DataTable dataTable) throws InterruptedException {
        Map<String, String> map = dataTable.asMap(String.class, String.class);
        lastCheckInToken  = map.getOrDefault("check_in", "today");
        lastCheckOutToken = map.getOrDefault("check_out", "tomorrow");
        homePageObject.setBookingDates(lastCheckInToken, lastCheckOutToken);
    }

    @When("I click the {string} button")
    public void i_click_the_button(String buttonLabel) throws InterruptedException {
        homePageObject.clickButtonByLabel(buttonLabel);
    }
    
    @Then("I should be on the Single room page {string} {string} {string}")
    public void i_should_be_on_the_single_room_page(String single, String doubleRoom, String suite) {
//    	smoke check – the Single room page has /reservation/1 in the path
        Assert.assertTrue(homePageObject.currentUrl().contains("/reservation/"),
                "Not on Single room reservation page. URL = " + homePageObject.currentUrl());
        homePageObject.verifyRoomPageTitle(single, doubleRoom, suite);
    }

    @Then("the page URL should contain {string}")
    public void the_page_url_should_contain(String roomUrlFragmentToken) {
        // validates both checkin/checkout from previously set tokens
        // and the specific fragment token from Examples (e.g., "tomorrow", "plus_2")
        homePageObject.verifyReservationUrl(lastCheckInToken, lastCheckOutToken, roomUrlFragmentToken);
    }
    
}
