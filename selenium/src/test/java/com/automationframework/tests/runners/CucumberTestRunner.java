package com.automationframework.tests.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

@Listeners({
  com.automationframework.tests.listeners.TestListener.class,
  com.automationframework.tests.listeners.AllureHtmlAfterSuite.class
})
@CucumberOptions(
	    features = "src/test/resources/features",
	    glue = "com.automationframework.tests.stepdefinitions",
	    plugin = {
	        "pretty",
	        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
	    },
	    monochrome = true,
	    tags = "@SmokeTestHomePage or @Sanity" // or @HomePage or 
	)

public class CucumberTestRunner extends AbstractTestNGCucumberTests {

    // Parallelize scenarios if needed
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
