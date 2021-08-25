package com.prac.framework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/***
 * Test class holds few variables which are required while logging test status
 * 
 * @author arvin
 *
 */
public class Test {

	/**
	 * holds step description
	 */
	private String stepDescription = "";
	/**
	 * holds expected value
	 */
	private String expectedValue = "";
	/**
	 * holds actual value
	 */
	private String actualValue = "";
	/**
	 * log status holds status of log like PASS/FAIL/SKIP
	 */
	private String logStatus = "";
	/**
	 * attachment path is hold in attachment variable
	 */
	private String attachment = "";
	/**
	 * evidenceTime is generated at runtime to get time at which statement was
	 * logged
	 */
	private String evidenceTime = "";

	/**
	 * to format evidence date as HH:mm:ss dd/MM/yy
	 */
	private SimpleDateFormat reportUiDateFormat = ListenerClass.reportUiDateFormat;

	/***
	 * constructor to create log with required values
	 * 
	 * @param stepDescription test step name
	 * @param expectedValue   expected value
	 * @param actualValue     actual value
	 * @param logStatus       log status i.e. PASS/FAIL/SKIP
	 * @param attachment      url of attachment
	 */
	public Test(String stepDescription, String expectedValue, String actualValue, String logStatus, String attachment) {
		super();
		this.stepDescription = stepDescription;
		this.expectedValue = expectedValue;
		this.actualValue = actualValue;
		this.logStatus = logStatus;
		this.attachment = attachment;
		this.evidenceTime = reportUiDateFormat.format(Calendar.getInstance().getTime());
	}

	/**
	 * @return Test class Object the stepDescription
	 */
	public String getStepDescription() {
		return stepDescription;
	}

	/**
	 * @return Test class Object the expectedValue
	 */
	public String getExpectedValue() {
		return expectedValue;
	}

	/**
	 * @return Test class Object the actualValue
	 */
	public String getActualValue() {
		return actualValue;
	}

	/**
	 * @return Test class Object the logStatus
	 */
	public String getLogStatus() {
		return logStatus;
	}

	/**
	 * @return Test class Object the attachment
	 */
	public String getAttachment() {
		return attachment;
	}

	/**
	 * @return Test class Object the evidenceTime
	 */
	public String getEvidenceTime() {
		return evidenceTime;
	}

	/***
	 * method helps in logging INFO statement for particular test case based on need
	 * 
	 * @param stepDescription test step description
	 * @param expectedValue   additional information
	 * @return Test class Object
	 */
	public static synchronized Test logInfo(String stepDescription, String expectedValue) {
		return new Test(stepDescription, expectedValue, "", Constants.Reporting.INFO, null);
	}

	/***
	 * method helps in logging SKIP statement for particular test case
	 * 
	 * @param stepDescription step description 
	 * @param expectedValue expected value 
	 * @return Test class Object 
	 */
	public static synchronized Test logSkip(String stepDescription, String expectedValue) {
		return new Test(stepDescription, expectedValue, "", Constants.Reporting.SKIP, null);
	}

	/***
	 * to log PASS statement where expected and actuals are one and the same
	 * 
	 * @param stepDescription step description 
	 * @param expectedValue expected value 
	 * @return Test class Object 
	 */
	public static synchronized Test logPass(String stepDescription, String expectedValue) {
		return new Test(stepDescription, expectedValue, expectedValue, Constants.Reporting.PASS, null);
	}

	/***
	 * method to log error as FAIL
	 * 
	 * @param stepDescription step description
	 * @param expectedValue   expected value which would be exception/error
	 *                        statement
	 * @return Test class Object
	 */
	public static synchronized Test logError(String stepDescription, String expectedValue) {
		return new Test(stepDescription, expectedValue, "", Constants.Reporting.FAIL, null);
	}

	/***
	 * log statement status is evaluated based on comparison of expected and actual
	 * 
	 * @param stepDescription step description
	 * @param expectedValue   expected value
	 * @param actualValue     actual value
	 * @return Test class Object
	 */
	public static synchronized Test log(String stepDescription, String expectedValue, String actualValue) {
		return new Test(stepDescription, expectedValue, actualValue,
				(expectedValue.equals(actualValue) ? Constants.Reporting.PASS : Constants.Reporting.FAIL), null);
	}

	/***
	 * log statement with status of what we received as parameter
	 * 
	 * @param stepDescription step description
	 * @param expectedValue   expected value
	 * @param actualValue     actual value
	 * @param logStatus       log status PASS/FAIL/SKIP
	 * @return Test class Object
	 */
	public static synchronized Test log(String stepDescription, String expectedValue, String actualValue,
			String logStatus) {
		return new Test(stepDescription, expectedValue, actualValue, logStatus, null);
	}

	/***
	 * 
	 * log statement with status of what we received as parameter
	 * 
	 * @param stepDescription step description
	 * @param expectedValue   expected value
	 * @param actualValue     actual value
	 * @param logStatus       log status PASS/FAIL/SKIP
	 * @param attachment      path of attachment
	 * @return Test class Object
	 */
	public static synchronized Test log(String stepDescription, String expectedValue, String actualValue,
			String logStatus, String attachment) {
		return new Test(stepDescription, expectedValue, actualValue, logStatus, attachment);
	}

	/***
	 * toString() method give String value of object which shows values being holded
	 * by each variable
	 */
	@Override
	public String toString() {
		return "TestLog [stepDescription=" + stepDescription + ", expectedValue=" + expectedValue + ", actualValue="
				+ actualValue + ", logStatus=" + logStatus + ", attachment=" + attachment + "]";
	}

}
