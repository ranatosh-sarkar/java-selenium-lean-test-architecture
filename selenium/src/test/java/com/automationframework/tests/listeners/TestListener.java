package com.automationframework.tests.listeners;

import com.automationframework.utils.LoggerUtil;
import com.automationframework.utils.TestUtils;
import org.testng.*;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        LoggerUtil.info("TEST START: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LoggerUtil.info("TEST PASS: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LoggerUtil.error("TEST FAIL: " + result.getMethod().getMethodName(), result.getThrowable());
        TestUtils.captureScreenshot(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LoggerUtil.warn("TEST SKIP: " + result.getMethod().getMethodName());
    }

    @Override public void onStart(ITestContext context) { }
    @Override public void onFinish(ITestContext context) { }
}
