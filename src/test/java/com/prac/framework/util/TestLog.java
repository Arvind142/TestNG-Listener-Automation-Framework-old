package com.prac.framework.util;

import static org.testng.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestLog {

	// testDetails
	private String testCaseName = "";
	private String stepDescription = "";
	private String expectedValue = "";
	private String actualValue = "";
	private String logStatus = "";
	private String attachment = "";

	// formating date
	private SimpleDateFormat reportUiDateFormat = ListenerClass.reportUiDateFormat;

	
	
	public TestLog(String testCaseName, String stepDescription, String expectedValue, String actualValue,
			String logStatus) {
		super();
		this.testCaseName = testCaseName;
		this.stepDescription = stepDescription;
		this.expectedValue = expectedValue;
		this.actualValue = actualValue;
		this.logStatus = logStatus;
	}

	public TestLog(String testCaseName, String stepDescription, String expectedValue, String actualValue,
			String logStatus, String attachment) {
		super();
		this.testCaseName = testCaseName;
		this.stepDescription = stepDescription;
		this.expectedValue = expectedValue;
		this.actualValue = actualValue;
		this.logStatus = logStatus;
		this.attachment = attachment;
	}

	/**
	 * @return the testCaseName
	 */
	public String getTestCaseName() {
		return testCaseName;
	}

	/**
	 * @param testCaseName the testCaseName to set
	 */
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	/**
	 * @return the stepDescription
	 */
	public String getStepDescription() {
		return stepDescription;
	}

	/**
	 * @param stepDescription the stepDescription to set
	 */
	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}

	/**
	 * @return the expectedValue
	 */
	public String getExpectedValue() {
		return expectedValue;
	}

	/**
	 * @param expectedValue the expectedValue to set
	 */
	public void setExpectedValue(String expectedValue) {
		this.expectedValue = expectedValue;
	}

	/**
	 * @return the actualValue
	 */
	public String getActualValue() {
		return actualValue;
	}

	/**
	 * @param actualValue the actualValue to set
	 */
	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}

	/**
	 * @return the logStatus
	 */
	public String getLogStatus() {
		return logStatus;
	}

	/**
	 * @param logStatus the logStatus to set
	 */
	public void setLogStatus(String logStatus) {
		this.logStatus = logStatus;
	}

	/**
	 * @return the attachment
	 */
	public String getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	/***
	 * log info statements
	 * 
	 * @param className expects tc identifier
	 * @param steps     steps for info
	 * @param details   details on info statement
	 */
	public synchronized void log(String className, String steps, String details) {
		String log = "";
		log = steps + Constants.Reporting.seprator + details + Constants.Reporting.seprator + ""
				+ Constants.Reporting.seprator + Constants.Reporting.INFO + Constants.Reporting.seprator + "[]"
				+ Constants.Reporting.seprator + reportUiDateFormat.format(Calendar.getInstance().getTime());
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
		String log = "";
		log = steps + Constants.Reporting.seprator + expected + Constants.Reporting.seprator + actual
				+ Constants.Reporting.seprator
				+ (expected.equals(actual) ? Constants.Reporting.PASS : Constants.Reporting.FAIL)
				+ Constants.Reporting.seprator + "[]" + Constants.Reporting.seprator
				+ reportUiDateFormat.format(Calendar.getInstance().getTime());
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
		String log = "";
		log = steps + Constants.Reporting.seprator + expected + Constants.Reporting.seprator + actual
				+ Constants.Reporting.seprator + status + Constants.Reporting.seprator + "[]"
				+ Constants.Reporting.seprator + reportUiDateFormat.format(Calendar.getInstance().getTime());
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
		String log = "";
		log = steps + Constants.Reporting.seprator + expected + Constants.Reporting.seprator + actual
				+ Constants.Reporting.seprator + status + Constants.Reporting.seprator + attachmant
				+ Constants.Reporting.seprator + reportUiDateFormat.format(Calendar.getInstance().getTime());
	}

	@Override
	public String toString() {
		return "TestLog [testCaseName=" + testCaseName + ", stepDescription=" + stepDescription + ", expectedValue="
				+ expectedValue + ", actualValue=" + actualValue + ", logStatus=" + logStatus + ", attachment="
				+ attachment + "]";
	}

}
