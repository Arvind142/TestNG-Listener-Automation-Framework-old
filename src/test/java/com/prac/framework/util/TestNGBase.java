package com.prac.framework.util;

import com.prac.main.BusinessFunction;
import static org.testng.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/***
 * class consist of few methods calls required for framework to be initialized
 * 
 * @author arvin
 *
 */
public class TestNGBase {
	/** reporting vars **/
	private Map<String, List<Test>> classLevel = null;
	private List<Test> testLevel = null;
	private Test log = null;
	protected BusinessFunction businessFunction = new BusinessFunction();

	// used for testCase logging
	public String className = "";
	public String testName = "";

	// formating date
	SimpleDateFormat reportUiDateFormat = ListenerClass.reportUiDateFormat;

	/***
	 * below method is to initialize required framework level variable for easier
	 * work
	 */
	@BeforeClass
	public void beforeClass() {
		businessFunction.readApplicaitonLevelProperty();
		businessFunction.readEnvironmentLevelProperty();
		classLevel = new ConcurrentHashMap<String, List<Test>>();
		className = this.getClass().getSimpleName();
	}

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

	/***
	 * log info statements
	 * 
	 * @param className expects tc identifier
	 * @param steps     steps for info
	 * @param details   details on info statement
	 */
	public synchronized void log(String className, String steps, String details) {
		log = Test.logInfo(steps, details);
		addLogToMap(className, log);
	}

	/***
	 * used for logging purpose where code decides on pass/fail
	 * 
	 * @param className tc identifier
	 * @param steps     step description
	 * @param expected  expected value
	 * @param actual    actual value
	 */
	public synchronized void log(String className, String steps, String expected, String actual) {
		log = Test.log(steps, expected, actual);
		addLogToMap(className, log);
		assertEquals(expected, actual);
	}

	/***
	 * log to report check point with statement status as given by user
	 * 
	 * @param className tc identifier
	 * @param steps     step description
	 * @param expected  expected value
	 * @param actual    actual value
	 * @param status    Pass/Fail/Info/Skip
	 */
	public synchronized void log(String className, String steps, String expected, String actual, String status) {
		log = Test.log(steps, expected, actual, status);
		addLogToMap(className, log);
		if (status.equals(Constants.Reporting.FAIL)) {
			assertEquals(expected, actual);
		}
	}

	/***
	 * log to report check point with statement status as given by user
	 * 
	 * @param className  tc identifier
	 * @param steps      step description
	 * @param expected   expected value
	 * @param actual     actual value
	 * @param status     Pass/Fail/Info/Skip
	 * @param attachmant attachment path
	 */
	public synchronized void log(String className, String steps, String expected, String actual, String status,
			String attachmant) {
		log = Test.log(steps, expected, actual, status, attachmant);
		addLogToMap(className, log);
		if (status.equals(Constants.Reporting.FAIL)) {
			assertEquals(expected, actual);
		}
	}

	/**
	 * add test level logs to classLevel log variable
	 * 
	 * 
	 * @param className expects classname - which is methodname/methodname + first
	 *                  parameter name
	 * @param log       expects logs statement in string format
	 */
	public synchronized void addLogToMap(String className, Test log) {
		if (classLevel.containsKey(className)) {
			// already exist
			if (classLevel.get(className) != null) {
				// getting list out of it
				testLevel = classLevel.get(className);
			} else {
				// creating new list
				testLevel = new ArrayList<Test>();
			}
			// adding log and updating map
			testLevel.add(log);
			classLevel.replace(className, testLevel);
		} else {
			// creating list and updating it in map
			testLevel = new ArrayList<Test>();
			testLevel.add(log);
			classLevel.put(className, testLevel);
		}
	}

}
