package com.prac.util;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.prac.main.BusinessFunction;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

public class BaseClass {
	public WebDriver driver;
	public BusinessFunction businessFunction = new BusinessFunction();

	// testCase are stored in this variale at runtime
	public String caseName = null;

	// Test DataObjectArray
	public Object[][] testData = null;

//	@Test
//	public void f() {
//
//		businessFunction.startTestCaseReporting(caseName);
//		businessFunction.initializeWebDriver();
//		businessFunction.openURL("https://google.com");
//		System.out.println("Navigation success!!!");
//		businessFunction.destroyWebDriver();
//		businessFunction.logResult("Browser Startup", "'Browser should work'", "'Browser worked'", Status.PASS);
//		businessFunction.stopTestCaseReporting();
//	}

	@BeforeSuite
	public void beforeSuite() {
		businessFunction.readApplicaitonLevelProperty();
		businessFunction.readEnvironmentLevelProperty();
		businessFunction.creatingReportFolder();
	}

	@AfterSuite
	public void afterSuite() {
	}

}
