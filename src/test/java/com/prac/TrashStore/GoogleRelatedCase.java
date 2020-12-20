package com.prac.TrashStore;

import org.testng.annotations.Test;

import com.prac.util.BaseClass;
import com.prac.util.Constants;

public class GoogleRelatedCase extends BaseClass {
	@Test
	public void f() {
		businessFunction.startTestCaseReporting("LOL");
		businessFunction.initializeWebDriver();
		businessFunction.openURL("https://google.com");
		System.out.println("Navigation success!!!");
		businessFunction.destroyWebDriver();
		businessFunction.logResult("Browser Startup", "'Browser should work'", "'Browser worked'",
				Constants.Reporting.PASS);
		businessFunction.stopTestCaseReporting();
	}
	@Test
	public void fup() {
		businessFunction.startTestCaseReporting("XDDD");
		businessFunction.initializeWebDriver();
		businessFunction.openURL("https://google.com");
		System.out.println("Navigation success!!!");
		businessFunction.destroyWebDriver();
		businessFunction.logResult("Browser Startup", "'Browser should work'", "'Browser worked'",Constants.Reporting.PASS);
		businessFunction.stopTestCaseReporting();
	}
}
