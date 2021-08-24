package com.prac.framework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Test {

	/**
	 * variables to hold log values
	 */
	private String stepDescription = "";
	private String expectedValue = "";
	private String actualValue = "";
	private String logStatus = "";
	private String attachment = "";
	private String evidenceTime = "";

	/**
	 * to format evidence date as HH:mm:ss dd/MM/yy
	 */
	private SimpleDateFormat reportUiDateFormat = ListenerClass.reportUiDateFormat;

	/***
	 * constructor to create log with required values
	 * 
	 * @param stepDescription
	 * @param expectedValue
	 * @param actualValue
	 * @param logStatus
	 * @param attachment
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
	 * @return the stepDescription
	 */
	public String getStepDescription() {
		return stepDescription;
	}

	/**
	 * @return the expectedValue
	 */
	public String getExpectedValue() {
		return expectedValue;
	}

	/**
	 * @return the actualValue
	 */
	public String getActualValue() {
		return actualValue;
	}

	/**
	 * @return the logStatus
	 */
	public String getLogStatus() {
		return logStatus;
	}

	/**
	 * @return the attachment
	 */
	public String getAttachment() {
		return attachment;
	}

	/**
	 * @return the evidenceTime
	 */
	public String getEvidenceTime() {
		return evidenceTime;
	}

	public static synchronized Test logInfo(String stepDescription, String expectedValue) {
		return new Test(stepDescription, expectedValue, "", Constants.Reporting.INFO, null);
	}

	public static synchronized Test logSkip(String stepDescription, String expectedValue) {
		return new Test(stepDescription, expectedValue, "", Constants.Reporting.SKIP, null);
	}

	public static synchronized Test logPass(String stepDescription, String expectedValue) {
		return new Test(stepDescription, expectedValue, expectedValue, Constants.Reporting.PASS, null);
	}

	public static synchronized Test logPass(String stepDescription, String expectedValue, String actualValue) {
		return new Test(stepDescription, expectedValue, actualValue, Constants.Reporting.PASS, null);
	}

	public static synchronized Test logError(String stepDescription, String expectedValue) {
		return new Test(stepDescription, expectedValue, "", Constants.Reporting.FAIL, null);
	}

	public static synchronized Test logFail(String stepDescription, String expectedValue, String actualValue) {
		return new Test(stepDescription, expectedValue, actualValue, Constants.Reporting.FAIL, null);
	}

	public static synchronized Test log(String stepDescription, String expectedValue, String actualValue) {
		return new Test(stepDescription, expectedValue, actualValue,
				(expectedValue.equals(actualValue) ? Constants.Reporting.PASS : Constants.Reporting.FAIL), null);
	}

	public static synchronized Test log(String stepDescription, String expectedValue, String actualValue,
			String logStatus) {
		return new Test(stepDescription, expectedValue, actualValue, logStatus, null);
	}

	public static synchronized Test log(String stepDescription, String expectedValue, String actualValue,
			String logStatus, String attachment) {
		return new Test(stepDescription, expectedValue, actualValue, logStatus, attachment);
	}

	@Override
	public String toString() {
		return "TestLog [stepDescription=" + stepDescription + ", expectedValue=" + expectedValue + ", actualValue="
				+ actualValue + ", logStatus=" + logStatus + ", attachment=" + attachment + "]";
	}

}
