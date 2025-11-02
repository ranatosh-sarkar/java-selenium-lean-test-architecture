package com.automationframework.tests.stepdefinitions;

import com.automationframework.core.ConfigManager;

import com.automationframework.core.DriverFactory;
import com.automationframework.core.EnvironmentManager;
import com.automationframework.enums.BrowserType;
import com.automationframework.utils.LoggerUtil;
import com.automationframework.utils.TestUtils;
import com.aventstack.extentreports.service.ExtentService;
import io.cucumber.java.*;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;
import java.nio.file.Path;

public class TestHooks {

	@BeforeAll
	public static void cleanAllureResults() {
	    try {
	        String base = System.getProperty("project.basedir", System.getProperty("user.dir"));
	        Path results = Paths.get(base, "allure-results");
	        boolean clean = Boolean.parseBoolean(System.getProperty("allure.clean.results", "true"));

	        if (clean && Files.isDirectory(results)) {
	            try (Stream<Path> s = Files.walk(results)) {
	                s.sorted(Comparator.reverseOrder())
	                 .filter(p -> !p.equals(results))            // delete contents, keep folder
	                 .forEach(p -> { try { Files.deleteIfExists(p); } catch (Exception ignored) {} });
	            }
	        }
	        Files.createDirectories(results);                  // ensure dir exists
	    } catch (Exception ignored) { }
	}
	
	@BeforeAll
	public static void addSystemInfo() {
	    // 1) Make sure configuration is loaded BEFORE we touch anything
	    String envName = com.automationframework.core.EnvironmentManager.getEnvironmentName();
	    com.automationframework.core.ConfigManager.getInstance(envName);

	    // 2) Build a nice filename: <project>_<env>_<yyyyMMdd_HHmmss>.html
	    String project = "AutomationFramework";
	    String ts = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
	    String safeEnv = (envName == null || envName.isBlank()) ? "local" : envName;

	    String outFile = String.format("reports/extent/%s_%s_%s.html", project, safeEnv, ts);
	    // IMPORTANT: set *before* the service instantiates reporters
	    System.setProperty("extent.reporter.spark.out", outFile);

	    // 3) System info for the Extent report
	    ExtentService.getInstance().setSystemInfo("Project", project);
	    ExtentService.getInstance().setSystemInfo("Environment", safeEnv);
	    ExtentService.getInstance().setSystemInfo("Executor", isCI() ? "GitHub Actions" : "Local");
	    ExtentService.getInstance().setSystemInfo("OS", System.getProperty("os.name"));
	    ExtentService.getInstance().setSystemInfo("Java", System.getProperty("java.version"));
	    ExtentService.getInstance().setSystemInfo("Browser",
	        com.automationframework.core.ConfigManager.get("browser").toUpperCase());
	}

	private static boolean isCI() {
	    return "true".equalsIgnoreCase(System.getenv("CI"))
	        || "true".equalsIgnoreCase(System.getenv("GITHUB_ACTIONS"));
	}

    /** Ensure output dirs and Allure result location exist before anything else. */
    @Before(order = -1)
    public void ensureDirs() {
        try {
            String base = System.getProperty("project.basedir", System.getProperty("user.dir"));
            Files.createDirectories(Paths.get(base, "allure-results"));
            Files.createDirectories(Paths.get(base, "reports"));
            Files.createDirectories(Paths.get(base, "logs"));

            if (System.getProperty("allure.results.directory") == null) {
                System.setProperty("allure.results.directory", Paths.get(base, "allure-results").toString());
            }
        } catch (Exception e) {
            LoggerUtil.warn("Failed to prepare output dirs: " + e.getMessage());
        }
    }

    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        LoggerUtil.info("=== Starting Scenario: " + scenario.getName() + " ===");

        String env = EnvironmentManager.getEnvironmentName();
        ConfigManager.getInstance(env);

        BrowserType browser;
        try {
            browser = BrowserType.valueOf(ConfigManager.get("browser").toUpperCase());
        } catch (Exception e) {
            LoggerUtil.warn("Invalid/missing 'browser' in config. Falling back to CHROME.");
            browser = BrowserType.CHROME;
        }

        // Start driver & navigate
        DriverFactory.setDriver(browser);
        String url = ConfigManager.get("base.url");
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null && url != null) {
            driver.get(url);
            LoggerUtil.info("Opened base URL: " + url);
        }
    }

    /** Optional: capture a screenshot only when a step fails. */
    @AfterStep
    public void afterStep(Scenario scenario) {
        if (scenario.isFailed()) {
            WebDriver driver = DriverFactory.getDriver();
            if (driver instanceof TakesScreenshot) {
                try {
                    byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    Allure.addAttachment("Failed step", new ByteArrayInputStream(png));
                    scenario.attach(png, "image/png", "Failed step");
                } catch (Exception ignored) {}
            }
        }
    }

    @After(order = 1)
    public void afterScenario(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();

        if (scenario.isFailed() && driver instanceof TakesScreenshot) {
            try {
                byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Failure Screenshot", new ByteArrayInputStream(png));
                scenario.attach(png, "image/png", "Failure Screenshot");
            } catch (Exception ignored) { }
            // File-based screenshot for quick local inspection
            TestUtils.captureScreenshot(scenario.getName().replaceAll("\\s+", "_"));
        }

        LoggerUtil.info("=== Finished Scenario: " + scenario.getName()
                + " (" + scenario.getStatus() + ") ===");

        if (driver != null) {
            DriverFactory.quitDriver();
        }
    }
}

