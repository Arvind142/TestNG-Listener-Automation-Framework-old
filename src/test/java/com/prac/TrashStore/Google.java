package com.prac.TrashStore;

import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import com.prac.util.Constants;
import com.prac.util.Constants.Reporting;
import com.prac.util.ResultLogClass;
import com.prac.util.TestNGBase;

public class Google extends TestNGBase {

	String className = this.getClass().getSimpleName();
	WebDriver driver = null;

	@DataProvider(name = "dataProvider")
	public Object[][] dataProvider() {
		return businessFunction.getDataFromExcel(className);
	}

	@Test(dataProvider = "dataProvider")
	public void f(@Optional(value = "TC00X") String TCName, Object[] testData) {
		// few reporting must

		try {
//			driver = businessFunction.initializeWebDriver("EDGE");
			log(className + "_" + TCName, "VAL1", "Browser started");
//			businessFunction.openURL(driver, businessFunction.environmentLevelProperty.getProperty("googleURL"));
			System.out.println("Navigation success!!!");
			log(className + "_" + TCName, "Navigation", "Successs Expected", "Success found", Constants.Reporting.PASS);
//			businessFunction.destroyWebDriver(driver);
		} catch (Exception e) {
			log(className + "_" + TCName, "Error found", "No exception expected", "exception found: " + e.getMessage(),
					Constants.Reporting.FAIL);
		}
	}

	@Test
	public void f2(@Optional(value = "TC00Y") String TCName) {
		// few reporting must
		try {
			log(className + "_" + TCName, "VAL1", "INFO-LOG");
			log(className + "_" + TCName, "VAL2", "B", "B");
			log(className + "_" + TCName, "VAL3", "A", "A", Constants.Reporting.PASS);
			log(className + "_" + TCName, "VAL4", "C", "D", Constants.Reporting.FAIL, "https://www.google.com");
		} catch (SkipException e) {
			log(className + "_" + TCName, "Skip Exception", "", "", Constants.Reporting.SKIP);
		} catch (Exception e) {
			log(className + "_" + TCName, "Error found", "No exception expected", "exception found: " + e.getMessage(),
					Constants.Reporting.FAIL);
		}
	}

	@Test
	public void f3(@Optional(value = "TC00Z") String TCName) throws Exception {
		// few reporting must
		throw new SkipException("Test Case to be skipped");
	}

	@Test
	public void f4(@Optional(value = "TC00A") String TCName) {
		// few reporting must
		try {
			throw new Exception("Some Error");
		} catch (SkipException e) {
			log(className + "_" + TCName, "Skip Exception", "", "", Constants.Reporting.SKIP);
		} catch (Exception e) {
			log(className + "_" + TCName, "Error found", "No exception expected", "exception found: " + e.getMessage(),
					Constants.Reporting.FAIL);
		}
	}
}