package com.prac.TrashStore;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.prac.util.TestNGBase;

public class Google extends TestNGBase {
	String className = this.getClass().getSimpleName();

	@DataProvider(name = "dataProvider")
	public Object[][] dataProvider() {
		return businessFunction.getDataFromExcel(className);
	}
	@Test(dataProvider = "dataProvider")
	public void f(Object[] testData) {
		businessFunction.startTestCaseReporting(testData[1].toString());
		businessFunction.initializeWebDriver();
		businessFunction.openURL(businessFunction.environmentLevelProperty.getProperty("googleURL"));
		System.out.println("Navigation success!!!");
		businessFunction.logResult("Browser Startup", "Google", businessFunction.driver.getTitle());
		businessFunction.destroyWebDriver();
		businessFunction.stopTestCaseReporting();
	}
}
