package com.prac.util;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ReporterClass implements ITestListener {
	// reporting
	protected static ExtentTest extentTest;
	protected static ExtentReports extentReports;
	protected static ExtentSparkReporter extentSparkReporter;

	@Override
	public void onTestStart(ITestResult result) {
		extentTest = extentReports.createTest(result.getTestClass().getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		System.out.println("Success of test cases and its details are : " + result.getName());
	}

	@Override
	public void onTestFailure(ITestResult result) {
		System.out.println("Failure of test cases and its details are : " + result.getName());
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println("Skip of test cases and its details are : " + result.getName());
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println("Failure of test cases and its details are : " + result.getName());
	}

	@Override
	public void onStart(ITestContext context) {
		// creating report
		extentSparkReporter = new ExtentSparkReporter(Library.outputFolder + "/index.html");
		extentSparkReporter.config().setEncoding("utf-8");
		extentSparkReporter.config().setDocumentTitle("Execution status");
		extentSparkReporter.config().setReportName("Automation Test Report");
		extentSparkReporter.config().setTheme(Theme.DARK);
		extentReports = new ExtentReports();
		extentReports.setSystemInfo("Application Level Properties : ", "");
		for (Object key : Library.applicationLevelProperty.keySet())
			extentReports.setSystemInfo(key.toString(), Library.applicationLevelProperty.get(key).toString());
		extentReports.setSystemInfo("Environment Level Properties : ", "");
		for (Object key : Library.environmentLevelProperty.keySet())
			extentReports.setSystemInfo(key.toString(), Library.environmentLevelProperty.get(key).toString());
		extentReports.attachReporter(extentSparkReporter);
	}

	@Override
	public void onFinish(ITestContext context) {
		System.out.println("Failure of test cases and its details are : " + context.getName());
		extentReports.flush();
	}
}
