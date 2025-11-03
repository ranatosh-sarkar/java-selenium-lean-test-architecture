package com.automationframework.tests.listeners;

import org.testng.IExecutionListener;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class AllureHtmlReportsListener implements IExecutionListener {

  @Override public void onExecutionStart() { }

  @Override
  public void onExecutionFinish() {
    // --- Guards ---
    if (isCI()) {
      System.out.println("[AllureHtmlReportsListener] CI detected → skipping HTML build (handled by pipeline).");
      return;
    }
    if (!isAutoHtmlEnabled()) {
      System.out.println("[AllureHtmlReportsListener] -Dallure.auto.html=false → skipping HTML build.");
      return;
    }

    try {
      final String base = System.getProperty("user.dir");
      final String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      final String resultsDir = base + File.separator + "allure-results";
      final String reportDir  = base + File.separator + "reports" + File.separator + "allure-report-" + ts;

      System.out.println("[AllureHtmlReportsListener] Building Allure HTML from: " + resultsDir);

      InvocationRequest req = new DefaultInvocationRequest();
      req.setPomFile(new File(base, "pom.xml"));
      req.setGoals(Collections.singletonList("io.qameta.allure:allure-maven:2.12.0:report"));

      Properties props = new Properties();
      props.setProperty("allure.results.directory", resultsDir);
      props.setProperty("allure.report.directory",  reportDir);
      req.setProperties(props);

      Invoker invoker = new DefaultInvoker();

      // Prefer Maven Wrapper in repo; else fall back to MAVEN_HOME/M2_HOME
      File mvnExe = findMavenExecutable(findMavenHome());
      if (mvnExe != null && mvnExe.isFile()) {
        invoker.setMavenExecutable(mvnExe);
        System.out.println("[AllureHtmlReportsListener] Using Maven exec: " + mvnExe);
      } else {
        System.err.println("[AllureHtmlReportsListener] Maven/mvnw not found. " +
            "Install Maven or add Wrapper (mvnw) to the repo. Skipping HTML build.");
        return; // do not fail tests
      }

      InvocationResult res = invoker.execute(req);
      if (res.getExitCode() != 0) {
        System.err.println("[AllureHtmlReportsListener] Allure report FAILED. " +
            (res.getExecutionException() != null ? res.getExecutionException().getMessage() : ""));
      } else {
        System.out.println("[AllureHtmlReportsListener] Allure report ready: " + reportDir + File.separator + "index.html");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // ---------- helpers ----------

  private static boolean isCI() {
    // True on GitHub Actions and most CIs
    return "true".equalsIgnoreCase(System.getenv("CI"))
        || "true".equalsIgnoreCase(System.getenv("GITHUB_ACTIONS"));
  }

  private static boolean isAutoHtmlEnabled() {
    // Default true locally; you can disable via -Dallure.auto.html=false
    return Boolean.parseBoolean(System.getProperty("allure.auto.html", "true"));
  }

  private static File findMavenHome() {
    String[] envs = {"MAVEN_HOME", "M2_HOME"};
    for (String e : envs) {
      String v = System.getenv(e);
      if (v != null && !v.isBlank() && new File(v).isDirectory()) return new File(v);
    }
    String prop = System.getProperty("maven.home");
    if (prop != null && !prop.isBlank() && new File(prop).isDirectory()) return new File(prop);
    // If you add Maven Wrapper later, we’ll use it in findMavenExecutable()
    return null;
  }

  private static File findMavenExecutable(File mavenHome) {
    String base = System.getProperty("user.dir");
    boolean win = System.getProperty("os.name","").toLowerCase().contains("win");
    // Prefer project’s Maven Wrapper if present (no global Maven needed)
    File mvnw = new File(base, win ? "mvnw.cmd" : "mvnw");
    if (mvnw.isFile()) return mvnw;
    if (mavenHome != null) {
      File bin = new File(mavenHome, "bin");
      File exe = new File(bin, win ? "mvn.cmd" : "mvn");
      if (exe.isFile()) return exe;
    }
    return null;
  }
}

