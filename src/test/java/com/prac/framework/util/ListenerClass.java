package com.prac.framework.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.prac.framework.util.reporting.ExcelReport;
import com.prac.framework.util.reporting.JsonReport;
import com.prac.framework.util.reporting.Reporting;

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
	public static Map<String, List<TestLog>> testResultMap = new ConcurrentHashMap<String, List<TestLog>>();

	/**
	 * testResultMap holds test status for each test case executed
	 */
	public static Map<String, List<TestLog>> testCaseExecutionStatus = new ConcurrentHashMap<String, List<TestLog>>();

	/**
	 * suiteTimeStamps holds start and end time of suite execution
	 */
	public static Map<String, String> suiteTimeStamps = new ConcurrentHashMap<String, String>();

	/**
	 * suite execution status
	 */
	public static Map<String, Integer> suiteExecutionStatus = new ConcurrentHashMap<String, Integer>();

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
	public static final SimpleDateFormat reportUiDateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

	/***
	 * output file location
	 */
	public static String environmentOutputFolder = null;

	/***
	 * output file location
	 */
	public static File reportingFolder = null;

	/****
	 * holds values of property file
	 */
	public static Properties applicationLevelProperty = new Properties();

	/**
	 * reporting genration
	 */
	private Reporting report = null;

	private ExtentSparkReporter htmlReporter = null;

	private ExtentReports extentReport = null;

	public static Map<String, ExtentTest> htmlTestLogs = null;

	/**
	 * first method to be executed on start of suite execution
	 */
	@Override
	public void onTestStart(ITestResult result) {
		String methodName = getTestName(result);
		System.out.println("Test Started: " + methodName);
		ExtentTest test = extentReport.createTest(methodName);
		extentReport.attachReporter(htmlReporter);
		htmlTestLogs.put(methodName, test);
	}

	/**
	 * called when test case execution have no issue/failure
	 */
	@Override
	public void onTestSuccess(ITestResult result) {
		System.out.println("Test Success");
		setTestCaseTimeStamp(result);
	}

	/**
	 * called when test case failed
	 */
	@Override
	public void onTestFailure(ITestResult result) {
		System.out.println("Test Failed");
		setTestCaseTimeStamp(result);
	}

	/**
	 * called when test case is skipped
	 */
	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println("Test Skipped");
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
		// initializing properties file
		initializeApplicationLevelProperty();

		// initializing html reporting
		htmlReporter = new ExtentSparkReporter(reportingFolder.getPath() + "/result.html");
		extentReport = new ExtentReports();
		try {
			htmlReporter.loadXMLConfig("src/test/resources/extent-config.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Object key : applicationLevelProperty.keySet()) {
			extentReport.setSystemInfo(key.toString(), applicationLevelProperty.getProperty(key.toString()));
		}
		htmlTestLogs = new HashMap<String, ExtentTest>();

		// starting logger
		LoggingClass.startLogger(reportingFolder.getPath());

		suiteTimeStamps.put("suiteStartTime", reportUiDateFormat.format(Calendar.getInstance().getTime()));
	}

	/***
	 * suite execution complete with finish method
	 */
	@Override
	public void onFinish(ITestContext context) {

		suiteTimeStamps.put("suiteEndTime", reportUiDateFormat.format(Calendar.getInstance().getTime()));

		suiteExecutionStatus = getSuiteStatus();
		// printing suite execution time stamps
		System.out.println("===============================================");
		System.out.println("Suite Execution Time");
		System.out.println("start time:\t" + suiteTimeStamps.get("suiteEndTime"));
		System.out.println("end time:  \t" + suiteTimeStamps.get("suiteStartTime"));
		System.out.println();
		System.out.println("Total tests run: " + suiteExecutionStatus.get("Total") + ", Passes: "
				+ suiteExecutionStatus.get("Passed") + ", Failures: " + suiteExecutionStatus.get("Failed") + ", Skips: "
				+ suiteExecutionStatus.get("Skipped"));
		System.out.println("===============================================");
		System.out.println("Test details");
		for (String tc : testResultMap.keySet()) {
			System.out.println("Status: " + getTestCaseStatus(testResultMap.get(tc)) + " => " + tc);
		}

		if (applicationLevelProperty.get("jsonReport").toString().equals("Yes")) {
			report = new JsonReport();
			System.out.println(report.generateReport(reportingFolder) ? "Json export success!" : "Json export failed!");
		}

		if (applicationLevelProperty.get("excelReport").toString().equals("Yes")) {
			report = new ExcelReport();
			System.out
					.println(report.generateReport(reportingFolder) ? "Excel export success!" : "Excel export failed!");
		}

		extentReport.flush();
	}

	/***
	 * 
	 * @param result    ITestResult to help in identification on tc name and status
	 * @param statement statement to log
	 */
	public void setLog(ITestResult result, String statement) {
		// adding log for timeoutCase
		List<TestLog> singleLog = new ArrayList<TestLog>();
		String methodName = getTestName(result);
		if (testResultMap.containsKey(methodName)) {
			singleLog = testResultMap.get(methodName);
		} else {
			singleLog = new ArrayList<TestLog>();
		}

		ExtentTest test = ListenerClass.htmlTestLogs.get(methodName);
		if (statement.contains("TESTNG: Skip")) {
			LoggingClass.log.warning(methodName + ", Skipped");
			test.log(Status.SKIP, statement);
			singleLog.add(TestLog.logSkip("Test execution", statement));
		} else {
			LoggingClass.log.severe(methodName + ", " + statement);
			test.log(Status.FAIL, statement);
			singleLog.add(TestLog.logError("Test Execution", statement));
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
		testExecutionDates = new ArrayList<Date>();
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

	public String getTestName(ITestResult result) {
		String className = result.getInstanceName().replace(".", ">");
		String methodName = className.split(">")[className.split(">").length - 1];
		if (result.getParameters().length > 0) {
			methodName += "." + result.getMethod().getMethodName() + "."
					+ getTestCaseNameConverted(result.getParameters()[0]);
		} else {
			methodName += "." + result.getMethod().getMethodName();
		}
		return methodName;
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
				if (obj instanceof Object[]) {
					Object[] objects = (Object[]) obj;
					return String.valueOf(objects[0]);
				} else {
					Object objects = (Object) obj;
					return String.valueOf(objects);
				}
			} catch (ClassCastException e) {
				return (String) obj;
			}
		} else if (obj instanceof String) {
			try {
				if (obj instanceof String[]) {
					String[] objects = (String[]) obj;
					return String.valueOf(objects[0]);
				} else {
					String objects = (String) obj;
					return String.valueOf(objects);
				}
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
			switch (getTestCaseStatus(testResultMap.get(testCase))) {
			case "PASS":
				pass++;
				break;
			case "FAIL":
				fail++;
				break;
			case "SKIP":
				skip++;
				break;
			default:
				break;
			}
		}
		suiteStatus.put("Passed", pass);
		suiteStatus.put("Failed", fail);
		suiteStatus.put("Skipped", skip);
		suiteStatus.put("Total", pass + fail + skip);
		return suiteStatus;
	}

	/**
	 * to get test case status based on test logs
	 * 
	 * @param logs test logs
	 * @return color i.e. success - PASS, danger - fail, warning - skip
	 */
	public static String getTestCaseStatus(List<TestLog> logs) {
		for (TestLog log : logs) {
			if (log.getLogStatus().equals(Constants.Reporting.FAIL)) {
				return "FAIL";
			} else if (log.getLogStatus().equals(Constants.Reporting.SKIP)) {
				return "SKIP";
			}
		}
		return "PASS";
	}

	public void initializeApplicationLevelProperty() {
		// loading applicationLevelConfig.properties
		try (InputStream ins = getClass().getClassLoader().getResource("ApplicationLevelConfig.properties")
				.openStream()) {
			// loading properties
			applicationLevelProperty.load(ins);
			if (applicationLevelProperty.keySet().size() == 0) {
				throw new Exception("Invalid Property file");
			}

			// creating environment folder if not present
			environmentOutputFolder = "test-output/" + applicationLevelProperty.get("Environment") + "/";
			if (!new File(environmentOutputFolder).exists())
				new File(environmentOutputFolder).mkdirs();

			// here we create new reporting folder
			reportingFolder = new File(
					environmentOutputFolder + (new File(environmentOutputFolder).listFiles().length + 1));
			reportingFolder.mkdir();
			System.out.println("Reporting folder: " + reportingFolder);
		} catch (FileNotFoundException e) {
			System.out.println("application level property file missing!");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}