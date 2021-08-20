package com.prac.util;

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
 * BaseClass class consist of few methods calls required for framework to be
 * initialized
 * 
 * @author arvin
 *
 */
public class TestNGBase {
	/** reporting vars **/
	private Map<String, List<String>> classLevel = null;
	private List<String> testLevel = null;
	protected BusinessFunction businessFunction = new BusinessFunction();

	// used for testCase logging
	public String className = "";
	public String testName = "";

	// formating time
	SimpleDateFormat reportUiDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy");

	/***
	 * below method is to initialize required framework level variable for easier
	 * work
	 */
	@BeforeClass
	public void beforeClass() {
		businessFunction.readApplicaitonLevelProperty();
		businessFunction.readEnvironmentLevelProperty();
		classLevel = new ConcurrentHashMap<String, List<String>>();
		className = this.getClass().getSimpleName();
	}

	@AfterClass
	public void afterClass() {
		// updating log in core reporting
		for (String key : classLevel.keySet()) {
			ListenerClass.testResultMap.put(key, classLevel.get(key));
		}
		System.out.println("we can check here");
	}

	/***
	 * format logs
	 * 
	 * @return
	 */
	public synchronized void log(String className, String steps, String details) {
		String log = "";
		log = steps + Constants.Reporting.seprator + details + Constants.Reporting.seprator + ""
				+ Constants.Reporting.seprator + Constants.Reporting.INFO + Constants.Reporting.seprator + "[]"
				+ Constants.Reporting.seprator + reportUiDateFormat.format(Calendar.getInstance().getTime());
		addLogToMap(className, log);
	}

	public synchronized void log(String className, String steps, String expected, String actual) {
		String log = "";
		log = steps + Constants.Reporting.seprator + expected + Constants.Reporting.seprator + actual
				+ Constants.Reporting.seprator
				+ (expected.equals(actual) ? Constants.Reporting.PASS : Constants.Reporting.FAIL)
				+ Constants.Reporting.seprator + "[]" + Constants.Reporting.seprator
				+ reportUiDateFormat.format(Calendar.getInstance().getTime());
		addLogToMap(className, log);
		assertEquals(expected, actual);
	}

	public synchronized void log(String className, String steps, String expected, String actual, String status) {
		String log = "";
		log = steps + Constants.Reporting.seprator + expected + Constants.Reporting.seprator + actual
				+ Constants.Reporting.seprator + status + Constants.Reporting.seprator + "[]"
				+ Constants.Reporting.seprator + reportUiDateFormat.format(Calendar.getInstance().getTime());
		addLogToMap(className, log);
		if (status.equals(Constants.Reporting.FAIL)) {
			assertEquals(expected, actual);
		}
	}

	public synchronized void log(String className, String steps, String expected, String actual, String status,
			String attachmant) {
		String log = "";
		log = steps + Constants.Reporting.seprator + expected + Constants.Reporting.seprator + actual
				+ Constants.Reporting.seprator + status + Constants.Reporting.seprator + attachmant
				+ Constants.Reporting.seprator + reportUiDateFormat.format(Calendar.getInstance().getTime());
		addLogToMap(className, log);
		if (status.equals(Constants.Reporting.FAIL)) {
			assertEquals(expected, actual);
		}
	}

	public synchronized void addLogToMap(String className, String log) {
		if (classLevel.containsKey(className)) {
			// already exist
			if (classLevel.get(className) != null) {
				// getting list out of it
				testLevel = classLevel.get(className);
			} else {
				// creating new list
				testLevel = new ArrayList<String>();
			}
			// adding log and updating map
			testLevel.add(log);
			classLevel.replace(className, testLevel);
		} else {
			// creating list and updating it in map
			testLevel = new ArrayList<String>();
			testLevel.add(log);
			classLevel.put(className, testLevel);
		}
	}

}
