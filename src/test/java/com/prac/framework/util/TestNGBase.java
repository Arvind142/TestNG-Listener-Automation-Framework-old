package com.prac.framework.util;

import com.prac.main.BusinessFunction;
import static org.testng.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/***
 * class contains few reporting variables and methods
 * 
 * @author arvin
 *
 */
public class TestNGBase {
	/***
	 * classLevel map holds details of test case execution at class level
	 */
	private Map<String, List<Test>> classLevel = null;
	/***
	 * testLevel list holds logs of each test case
	 */
	private List<Test> testLevel = null;
	/**
	 * Test class variable for test logging
	 */
	private Test log = null;
	/**
	 * businessfunction variable to work with businessFunction methods
	 */
	protected BusinessFunction businessFunction = new BusinessFunction();

	/**
	 * holds class name
	 */
	public String className = "";
	/***
	 * hold test case name i.e methodName_paramName if applicable
	 */
	public String testName = "";

	/**
	 * date format to give date in correct format in evidence
	 */
	SimpleDateFormat reportUiDateFormat = ListenerClass.reportUiDateFormat;

	/***
	 * before class to initialize few properties so that it helps user in getting
	 * details in easier way from property file
	 */
	@BeforeClass
	public void beforeClass() {
		businessFunction.readApplicaitonLevelProperty();
		businessFunction.readEnvironmentLevelProperty();
		classLevel = new ConcurrentHashMap<String, List<Test>>();
		className = this.getClass().getSimpleName();
	}

	/**
	 * AfterClass acts as finisher who will put all class level logs to listener
	 * class variable so that it can be used for reporting
	 */
	@AfterClass
	public void afterClass() {
		// updating log in core reporting
		for (String key : classLevel.keySet()) {
			// if key already exist for given case
			if (ListenerClass.testResultMap.containsKey(key)) {
				// below code is extremely important because of timeout/SKIP case
				// getting classLevel logs
				testLevel = new ArrayList<Test>();
				testLevel = classLevel.get(key);
				// getting suite level logs
				List<Test> tempList = ListenerClass.testResultMap.get(key);
				// appending suite level log into classLevel
				for (Test log : tempList) {
					testLevel.add(log);
				}
				ListenerClass.testResultMap.replace(key, testLevel);
			} else {
				ListenerClass.testResultMap.put(key, classLevel.get(key));
			}
		}
	}

	/**
	 * INFO log statement
	 * 
	 * @param testName testCaseName
	 * @param steps    step description
	 * @param details  details which is to be logged
	 */
	public synchronized void logInfo(String testName, String steps, String details) {
		log = Test.logInfo(steps, details);
		addLogToMap(testName, log);
	}

	/***
	 * SKIP log statement
	 * 
	 * @param testName testCaseName
	 * @param steps    step description
	 * @param details  details which is to be logged
	 */
	public synchronized void logSkip(String testName, String steps, String details) {
		log = Test.logSkip(steps, details);
		addLogToMap(testName, log);
	}

	/***
	 * PASS log statement
	 * 
	 * @param testName testCaseName
	 * @param steps    step description
	 * @param details  details which is to be logged
	 */
	public synchronized void logPass(String testName, String steps, String details) {
		log = Test.logPass(steps, details);
		addLogToMap(testName, log);
		assertEquals(details, details);
	}

	/***
	 * Error log statement
	 * 
	 * @param testName testCaseName
	 * @param steps    step description
	 * @param expected  details which is to be logged
	 */
	public synchronized void logError(String testName, String steps, String expected) {
		log = Test.logError(steps, expected);
		addLogToMap(testName, log);
		assertEquals(expected, "Error Found!");
	}

	/***
	 * log statement which evaluates status of log with expected and actual value
	 * 
	 * @param testName test caseName
	 * @param steps    step description
	 * @param expected expected value
	 * @param actual   actual value
	 */
	public synchronized void log(String testName, String steps, String expected, String actual) {
		log = Test.log(steps, expected, actual);
		addLogToMap(testName, log);
		assertEquals(expected, actual);
	}

	/***
	 * log statement status is taken as it is from method parameter variable status
	 * 
	 * @param testName test caseName
	 * @param steps    step description
	 * @param expected expected value
	 * @param actual   actual value
	 * @param status   log status
	 */
	public synchronized void log(String testName, String steps, String expected, String actual, String status) {
		log = Test.log(steps, expected, actual, status);
		addLogToMap(testName, log);
		if (status.equals(Constants.Reporting.FAIL)) {
			assertEquals(expected, actual);
		}
	}

	/***
	 * 
	 * @param testName   test caseName
	 * @param steps      step description
	 * @param expected   expected value
	 * @param actual     actual value
	 * @param status     log status
	 * @param attachmant path where evidence is extracted
	 */
	public synchronized void log(String testName, String steps, String expected, String actual, String status,
			String attachmant) {
		log = Test.log(steps, expected, actual, status, attachmant);
		addLogToMap(testName, log);
		if (status.equals(Constants.Reporting.FAIL)) {
			assertEquals(expected, actual);
		}
	}

	/***
	 * method puts test log into class level variable for better reporting
	 * 
	 * @param testName test case name
	 * @param log      statement with status
	 */
	public synchronized void addLogToMap(String testName, Test log) {
		if (classLevel.containsKey(testName)) {
			// already exist
			if (classLevel.get(testName) != null) {
				// getting list out of it
				testLevel = classLevel.get(testName);
			} else {
				// creating new list
				testLevel = new ArrayList<Test>();
			}
			// adding log and updating map
			testLevel.add(log);
			classLevel.replace(testName, testLevel);
		} else {
			// creating list and updating it in map
			testLevel = new ArrayList<Test>();
			testLevel.add(log);
			classLevel.put(testName, testLevel);
		}
	}

}
