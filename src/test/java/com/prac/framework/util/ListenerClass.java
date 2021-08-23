package com.prac.framework.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * backbone of framework with all listeners to perform some set of operations
 * based on need
 * 
 * @author arvin
 *
 */
public class ListenerClass implements ITestListener {

	// stores test results
	public static Map<String, List<String>> testResultMap = new ConcurrentHashMap<String, List<String>>();

	// holds starts and end time of test suite
	public static Map<String, Date> suiteTimeStamps = new ConcurrentHashMap<String, Date>();

	// holds time of each testcase start and Endtime
	public static Map<String, List<Date>> testTimeStamps = new ConcurrentHashMap<String, List<Date>>();

	// holds start and end time of one testcase at a time
	private List<Date> testExecutionDates = null;

	// formating time
	public static SimpleDateFormat reportUiDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");

	// logger
	// getting class name at runtime
	private static final String className = ListenerClass.class.getName();

	// logger initialized with MC
	public static final Logger logger = Logger.getLogger(className);

	// handlers i.e. file and console
	private static Handler consoleHandler = null, fileHandler = null;

	public ListenerClass() throws Exception {
		// turning off parent handler i.e. consoleHandler
		logger.setUseParentHandlers(false);

		// creating ConsoleHandler and setting level
		consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);

		// creating FileHandler and setting level
		fileHandler = new FileHandler("src/test/resource/" + className + ".log");
		fileHandler.setLevel(Level.ALL);

		// Adding handlers
		logger.addHandler(consoleHandler);
		logger.addHandler(fileHandler);
		
		logger.setLevel(Level.ALL);

	}

	@Override
	public void onTestStart(ITestResult result) {
		System.out.println(
				"Case Execution Test Started: " + result.getInstanceName() + "." + result.getMethod().getMethodName());
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
		setLogForTimeoutCase(result, "TESTNG: Skip");
		setTestCaseTimeStamp(result);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println("Case Execution onTestFailedButWithinSuccessPercentage");

	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		System.out.println("Case Execution onTestFailedWithTimeout");
		// to log entry for timeout case
		setLogForTimeoutCase(result, "TESTNG: Timeout error");
		onTestFailure(result);
	}

	@Override
	public void onStart(ITestContext context) {
		suiteTimeStamps.put("suiteStartTime", Calendar.getInstance().getTime());
		logger.log(Level.INFO, "Suite execution started");
	}

	@Override
	public void onFinish(ITestContext context) {

		suiteTimeStamps.put("suiteEndTime", Calendar.getInstance().getTime());
		System.out.println(
				"------------------------------------Case Execution Finish------------------------------------");
		System.out.println("Passed:" + context.getPassedTests().size());
		System.out.println("Failed:" + context.getFailedTests().size());
		System.out.println("Skipped:" + context.getSkippedTests().size());

		// creating html report
		HTMLReporting report = new HTMLReporting();
		report.createReport(context);

		// email report
		
		
		logger.log(Level.INFO, "Suite execution stopped");

	}

	/**
	 * method would set log for timeout cases where case execution crossed timeout
	 * limit
	 * 
	 * @param result
	 */
	public void setLogForTimeoutCase(ITestResult result, String statement) {
		// adding log for timeoutCase
		List<String> singleLog = new ArrayList<String>();
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
			singleLog.add("Test Execution" + Constants.Reporting.seprator + "[]" + Constants.Reporting.seprator
					+ statement + Constants.Reporting.seprator + Constants.Reporting.SKIP + Constants.Reporting.seprator
					+ "[]" + Constants.Reporting.seprator
					+ reportUiDateFormat.format(Calendar.getInstance().getTime()));
		} else {
			singleLog.add("Test Execution" + Constants.Reporting.seprator + "[]" + Constants.Reporting.seprator
					+ statement + Constants.Reporting.seprator + Constants.Reporting.FAIL + Constants.Reporting.seprator
					+ "[]" + Constants.Reporting.seprator
					+ reportUiDateFormat.format(Calendar.getInstance().getTime()));
		}
		if (testResultMap.containsKey(methodName)) {
			testResultMap.replace(methodName, singleLog);
		} else {
			testResultMap.put(methodName, singleLog);
		}
	}

	/**
	 * method helps in identifying and logging of start and end tiem for each test
	 * case
	 * 
	 * @param result
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