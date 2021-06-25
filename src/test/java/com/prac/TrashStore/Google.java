package com.prac.TrashStore;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.prac.util.Library;
import com.prac.util.TestNGBase;

public class Google extends TestNGBase {
	String className = this.getClass().getSimpleName();

	@DataProvider(name = "dataProvider")
	public Object[][] dataProvider() {
		return businessFunction.getDataFromExcel(className);
	}
	@Test(dataProvider = "dataProvider")
	public void f(Object[] testData) {
		businessFunction.initializeWebDriver();
		businessFunction.openURL(Library.environmentLevelProperty.getProperty("googleURL"));
		System.out.println("Navigation success!!!");
		businessFunction.logResult("Browser Startup", "Google", businessFunction.driver.getTitle());
		businessFunction.destroyWebDriver();
	}
}