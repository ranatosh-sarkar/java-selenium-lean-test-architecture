package com.automationframework.tests.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

@Listeners({
  com.automationframework.tests.listeners.TestListener.class,
  com.automationframework.tests.listeners.GenerateAllureHtml.class
})
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.automationframework.tests.stepdefinitions",
    plugin = {
        "pretty",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
        // Extent Spark adapter (adds local HTML report alongside Allure)
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true,
    tags = "@SmokeTestHomePage or @Sanity or @HomePage" //
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {

    // Set Extent Spark output file name at class-load time
    static {
        String ts = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new java.util.Date());
        System.setProperty(
            "extent.reporter.spark.out",
            "reports/extent-reports/reports_" + ts + ".html"
        );
    }

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
