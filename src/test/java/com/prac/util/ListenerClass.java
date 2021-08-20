package com.prac.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass implements ITestListener {

	// stores test results
	public static Map<String, List<String>> testResultMap = new ConcurrentHashMap<String, List<String>>();
	// holds starts and end time of test suite
	public static Map<String, Date> suiteTimeStamps = new ConcurrentHashMap<String, Date>();
	// holds time of each testcase start and Endtime
	public static Map<String, List<Date>> testTimeStamps = new ConcurrentHashMap<String, List<Date>>();
	// holds start and end time of one testcase at a time
	private List<Date> testExecutionDates = null;

	public ListenerClass() {
		System.out.println("Constructor started");
	}

	@Override
	public void onTestStart(ITestResult result) {
		System.out.println("Case Execution Test Started");
		System.out.println("Test Start Time: " + Calendar.getInstance().getTime());
		testExecutionDates = new ArrayList<Date>();
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		System.out.println("Case Execution Test Success");
		setTestCaseTimeStamp(result);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		System.out.println("Case Execution Test Failed");
		setTestCaseTimeStamp(result);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println("Case Execution Test Skipped");
		setTestCaseTimeStamp(result);
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
		suiteTimeStamps.put("suiteStartTime", Calendar.getInstance().getTime());
	}

	@Override
	public void onFinish(ITestContext context) {

		suiteTimeStamps.put("suiteEndTime", Calendar.getInstance().getTime());
		System.out.println("EndTime: " + Calendar.getInstance().getTime());
		System.out.println(
				"------------------------------------Case Execution Finish------------------------------------");
		System.out.println("Passed:" + context.getPassedTests().size());
		System.out.println("Failed:" + context.getFailedTests().size());
		System.out.println("Skipped:" + context.getSkippedTests().size());
		HTMLReporting report = new HTMLReporting();
		report.createReport(context);
	}

	public void setTestCaseTimeStamp(ITestResult result) {
		testExecutionDates.clear();
		Date date = null;
		date = new Date();
		date.setTime(result.getStartMillis());
		testExecutionDates.add(date);
		date=new Date();
		date.setTime(result.getEndMillis());
		testExecutionDates.add(date);
		if (result.getParameters().length > 0) {
			testTimeStamps.put(String.valueOf(result.getInstanceName() + "." + result.getMethod().getMethodName() + "."
					+ getTestCaseNameConverted(result.getParameters()[0])), testExecutionDates);
			System.out.println((result.getParameters().length > 0) + "->>"
					+ String.valueOf(result.getInstanceName() + "." + result.getMethod().getMethodName() + "."
							+ getTestCaseNameConverted(result.getParameters()[0])));
		} else {
			testTimeStamps.put(result.getInstanceName() + "." + result.getMethod().getMethodName(), testExecutionDates);
			System.out.println((result.getParameters().length > 0) + "->>" + result.getInstanceName() + "."
					+ result.getMethod().getMethodName());
		}
	}

	/**
	 * used to convert object into object[] based on requirement
	 * 
	 * @param obj
	 * @return
	 */
	public String getTestCaseNameConverted(Object obj) {
		if (obj instanceof Object) {
			try {
				Object[] objects = (Object[]) obj;
				return String.valueOf(objects[0]);
			} catch (ClassCastException e) {
				return (String) obj;
			}
		} else {
			return String.valueOf(obj);
		}
	}

}