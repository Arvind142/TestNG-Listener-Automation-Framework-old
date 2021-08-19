package com.prac.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass implements ITestListener {

	private Map<String, String> testResults = null;

	public ListenerClass() {
		System.out.println("Constructor started");
		testResults = new HashMap<String, String>();
	}

	@Override
	public void onTestStart(ITestResult result) {
		System.out.println("Case Execution Test Started");
		System.out.println("Test Start Time: " + Calendar.getInstance().getTime());

	}

	@Override
	public void onTestSuccess(ITestResult result) {
		System.out.println("Case Execution Test Success");
		testResults.put(result.getName(), getStringStatus(result.getStatus()));

	}

	@Override
	public void onTestFailure(ITestResult result) {
		System.out.println("Case Execution Test Failed");
		testResults.put(result.getName(), getStringStatus(result.getStatus()));

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println("Case Execution Test Skipped");
		testResults.put(result.getName(), getStringStatus(result.getStatus()));

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println("Case Execution onTestFailedButWithinSuccessPercentage");

	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		System.out.println("Case Execution onTestFailedWithTimeout");
		onTestFailure(result);
	}

	@Override
	public void onStart(ITestContext context) {
		System.out.println("Case Execution Start");
		System.out.println("StartTime: " + Calendar.getInstance().getTime());
	}

	@Override
	public void onFinish(ITestContext context) {
		System.out.println("EndTime: " + Calendar.getInstance().getTime());
		System.out.println(
				"------------------------------------Case Execution Finish------------------------------------");
		System.out.println("Passed:" + context.getPassedTests().size());
		System.out.println("Failed:" + context.getFailedTests().size());
		System.out.println("Skipped:" + context.getSkippedTests().size());

		for (String e : ResultLogClass.testResultMap.keySet()) {
			System.out.println(e + "->" + ResultLogClass.testResultMap.get(e));
		}
		this.toString();
		HTMLReporting report = new HTMLReporting();
		report.createReport(context);
	}

	/**
	 * method to return String value equivalent to int status
	 * 
	 * @param status
	 * @return
	 */
	public String getStringStatus(int status) {
		switch (status) {
		case ITestResult.SUCCESS:
			return "SUCCESS";
		case ITestResult.FAILURE:
			return "FAILURE";
		case ITestResult.SKIP:
			return "SKIP";
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		return "ListenerClass [testResults=" + testResults + "]";
	}

}