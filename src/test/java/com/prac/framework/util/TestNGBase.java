package com.prac.framework.util;

import static org.testng.Assert.assertTrue;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import com.prac.TrashStore.BusinessFunctions;
import com.prac.utils.Web;
import com.prac.utils.API;
import com.prac.utils.Database;

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
	private Map<String, List<TestLog>> classLevel = null;
	/***
	 * testLevel list holds logs of each test case
	 */
	private List<TestLog> testLevel = null;
	/**
	 * Test class variable for test logging
	 */
	private TestLog log = null;
	/**
	 * businessfunction variable to work with businessFunction methods
	 */
	protected BusinessFunctions businessFunction = new BusinessFunctions();

	/***
	 * web object to work with web methods
	 */
	protected Web web = new Web();

	/**
	 * API class object to work with api based method
	 */
	protected API api = new API();

	/**
	 * DataBase class object to work with db related methods
	 */
	protected Database db = new Database();

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
		businessFunction.readEnvironmentLevelProperty();
		classLevel = new ConcurrentHashMap<String, List<TestLog>>();
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
				testLevel = new ArrayList<TestLog>();
				testLevel = classLevel.get(key);
				// getting suite level logs
				List<TestLog> tempList = ListenerClass.testResultMap.get(key);
				// appending suite level log into classLevel
				for (TestLog log : tempList) {
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
		log = TestLog.logInfo(steps, details);
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
		log = TestLog.logSkip(steps, details);
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
		log = TestLog.logPass(steps, details);
		addLogToMap(testName, log);
	}

	/***
	 * Error log statement
	 * 
	 * @param testName testCaseName
	 * @param steps    step description
	 * @param expected details which is to be logged
	 */
	public synchronized void logError(String testName, String steps, String expected) {
		log = TestLog.logError(steps, expected);
		addLogToMap(testName, log);
	}

	/**
	 * to log result
	 * 
	 * @param <T>      any data type can be array/list/set
	 * @param testName test caseName
	 * @param steps    step description
	 * @param expected expected value
	 * @param actual   actual value
	 */
	public synchronized <T> void log(String testName, String steps, T expected, T actual) {
		log = TestLog.log(steps, expected, actual);
		addLogToMap(testName, log);
	}

	/***
	 * to log result
	 * 
	 * @param <T>        any data type can be array/list/set
	 * @param testName   test case name
	 * @param steps      step description
	 * @param expected   expected data
	 * @param actual     actual data
	 * @param attachmant evidence path
	 */
	public synchronized <T> void log(String testName, String steps, T expected, T actual, String attachmant) {
		log = TestLog.log(steps, expected, actual, attachmant);
		addLogToMap(testName, log);
	}

	/***
	 * method puts test log into class level variable for better reporting
	 * 
	 * @param testName test case name
	 * @param log      statement with status
	 */
	public synchronized void addLogToMap(String testName, TestLog log) {
		boolean failFound = (log.getLogStatus().equals(Constants.Reporting.FAIL)) ? true : false;
		if (log.getLogStatus().equals(Constants.Reporting.FAIL)) {
			LoggingClass.log.log(Level.SEVERE, testName + ":" + log);
		} else if (log.getLogStatus().equals(Constants.Reporting.WARNING)) {
			LoggingClass.log.log(Level.WARNING, testName + ":" + log);
		} else {
			LoggingClass.log.log(Level.INFO, testName + ":" + log);
		}
		if (classLevel.containsKey(testName)) {
			// already exist
			if (classLevel.get(testName) != null) {
				// getting list out of it
				testLevel = classLevel.get(testName);
			} else {
				// creating new list
				testLevel = new ArrayList<TestLog>();
			}
			// adding log and updating map
			testLevel.add(log);
			classLevel.replace(testName, testLevel);
		} else {
			// creating list and updating it in map
			testLevel = new ArrayList<TestLog>();
			testLevel.add(log);
			classLevel.put(testName, testLevel);
		}
		assertTrue(!failFound);
	}

}
