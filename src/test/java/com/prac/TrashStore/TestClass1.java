package com.prac.TrashStore;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.prac.framework.util.TestNGBase;

public class TestClass1 extends TestNGBase {
	@Test(enabled = true)
	public void webBasedMethod() {
		// test case name
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			logInfo(testName, "Execution started", "!!");
			WebDriver driver = web.initializeWebDriver("EDGE");
			web.openURL(driver, "https://google.com");
			Thread.sleep(1000);
			web.waitForPageLoad(driver);
			web.destroyWebDriver(driver);
			logInfo(testName, "Execution stopped", "!!");
		} catch (Exception e) {
			e.printStackTrace();
			logError(testName, "execution...", e.getMessage());
		}

	}
}
