package com.automationframework.tests.stepdefinitions;

import io.cucumber.java.en.Then;

import com.automationframework.pages.SanityTestPageObject;

public class SanityStepDefination {
	
	private final SanityTestPageObject sanityPageObject = new SanityTestPageObject();

	@Then("Enter my details {string} {string} {string} {string}")
	public void enter_my_details(String firstName, String lastName, String email, String phone) throws InterruptedException {
        sanityPageObject.enterUserDetails(firstName, lastName, email, phone);
        sanityPageObject.clickReserveNow();
        System.out.println("Passed: enter_my_details()");
	}

	@Then("I verify {string} message")
	public void i_verify_message(String expected) {
        sanityPageObject.verifyBookingConfirmed(expected);
        System.out.println("Passed: i_verify_message()");
	}
}
