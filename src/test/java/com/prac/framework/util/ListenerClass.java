package com.prac.framework.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * backbone of framework with all listeners to perform some set of operations
 * based on need
 * 
 * @author arvin
 *
 */
public class ListenerClass implements ITestListener {

	/**
	 * testResultMap holds test results for each case executed
	 */
	public static Map<String, List<Test>> testResultMap = new ConcurrentHashMap<String, List<Test>>();

	/**
	 * suiteTimeStamps holds start and end time of suite execution
	 */
	public static Map<String, Date> suiteTimeStamps = new ConcurrentHashMap<String, Date>();

	/**
	 * suite execution status
	 */
	public static Map<String, Integer> suiteStatus = new ConcurrentHashMap<String, Integer>();

	/**
	 * testTimeStamps holds test case start and end time execution
	 */
	public static Map<String, List<Date>> testTimeStamps = new ConcurrentHashMap<String, List<Date>>();

	/**
	 * list to put start and end time of test case execution
	 */
	private List<Date> testExecutionDates = null;

	/**
	 * SimpleDateFormat to give one common for to dates reflecting on report
	 */
	public static final SimpleDateFormat reportUiDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");

	/**
	 * first method to be executed on start of suite execution
	 */
	@Override
	public void onTestStart(ITestResult result) {
		System.out.println(
				"Case Execution Test Started: " + result.getInstanceName() + "." + result.getMethod().getMethodName());
		testExecutionDates = new ArrayList<Date>();

	}

	/**
	 * called when test case execution have no issue/failure
	 */
	@Override
	public void onTestSuccess(ITestResult result) {
		System.out.println("Case Execution Test Success");
		setTestCaseTimeStamp(result);
	}

	/**
	 * called when test case failed
	 */
	@Override
	public void onTestFailure(ITestResult result) {
		System.out.println("Case Execution Test Failed");
		setTestCaseTimeStamp(result);
	}

	/**
	 * called when test case is skipped
	 */
	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println("Case Execution Test Skipped");
		setLog(result, "TESTNG: Skip");
		setTestCaseTimeStamp(result);
	}

	/**
	 * test failed with success percentage
	 */
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println("Case Execution onTestFailedButWithinSuccessPercentage");

	}

	/**
	 * test execution failed with timeout
	 */
	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		System.out.println("Case Execution onTestFailedWithTimeout");
		// to log entry for timeout case
		setLog(result, "TESTNG: Timeout error");
		onTestFailure(result);
	}

	/**
	 * test execution started
	 */
	@Override
	public void onStart(ITestContext context) {
		suiteTimeStamps.put("suiteStartTime", Calendar.getInstance().getTime());
	}

	/***
	 * suite execution complete with finish method
	 */
	@Override
	public void onFinish(ITestContext context) {

		suiteTimeStamps.put("suiteEndTime", Calendar.getInstance().getTime());
		System.out.println(
				"------------------------------------Case Execution Finish------------------------------------");
		suiteStatus = getSuiteStatus();
		for (String s : suiteStatus.keySet()) {
			System.out.println(s + ": " + suiteStatus.get(s));
		}

		// creating html report
		HTMLReporting report = new HTMLReporting();
		report.createReport(context);

		// email report

	}

	/***
	 * 
	 * @param result    ITestResult to help in identification on tc name and status
	 * @param statement statement to log
	 */
	public void setLog(ITestResult result, String statement) {
		// adding log for timeoutCase
		List<Test> singleLog = new ArrayList<Test>();
		String methodName = result.getInstanceName().replace(".", "_");
		methodName = methodName.split("_")[methodName.split("_").length - 1];
		if (result.getParameters().length > 0) {
			methodName += "." + result.getMethod().getMethodName() + "."
					+ getTestCaseNameConverted(result.getParameters()[0]);
		} else {
			methodName += "." + result.getMethod().getMethodName();
		}
		if (testResultMap.containsKey(methodName)) {
			singleLog = testResultMap.get(methodName);
		}
		if (statement.contains("TESTNG: Skip")) {
			singleLog.add(Test.logSkip("Test execution", statement));
		} else {
			singleLog.add(Test.logError("Test Execution", statement));
		}
		if (testResultMap.containsKey(methodName)) {
			testResultMap.replace(methodName, singleLog);
		} else {
			testResultMap.put(methodName, singleLog);
		}
	}

	/**
	 * method helps in identifying and logging of start and end time for each test
	 * case
	 * 
	 * @param result ItestResult which will help method is identification of start
	 *               and end time of test case
	 */
	public void setTestCaseTimeStamp(ITestResult result) {
		testExecutionDates.clear();
		Date date = null;
		date = new Date();
		date.setTime(result.getStartMillis());
		testExecutionDates.add(date);
		date = new Date();
		date.setTime(result.getEndMillis());
		testExecutionDates.add(date);
		if (result.getParameters().length > 0) {
			testTimeStamps.put(String.valueOf(result.getInstanceName() + "." + result.getMethod().getMethodName() + "."
					+ getTestCaseNameConverted(result.getParameters()[0])), testExecutionDates);
		} else {
			testTimeStamps.put(result.getInstanceName() + "." + result.getMethod().getMethodName(), testExecutionDates);
		}
	}

	/**
	 * used to convert object into object[] based on requirement
	 * 
	 * @param obj parameter of method
	 * @return string value of testcaseName
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

	/**
	 * method gives test case status based logs of each test case
	 * 
	 * @return map which includes count on passed,failed and skipped cases
	 */
	public Map<String, Integer> getSuiteStatus() {
		Map<String, Integer> suiteStatus = new HashMap<>();
		int pass = 0, fail = 0, skip = 0;
		for (String testCase : testResultMap.keySet()) {
			switch (HTMLReporting.getTestCaseStatus(testResultMap.get(testCase))) {
			case "success":
				pass++;
				break;
			case "danger":
				fail++;
				break;
			case "warning":
				skip++;
				break;
			default:
				break;
			}
		}
		suiteStatus.put("Passed", pass);
		suiteStatus.put("Failed", fail);
		suiteStatus.put("Skipped", skip);
		return suiteStatus;
	}
}