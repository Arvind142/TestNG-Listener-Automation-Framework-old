package com.prac.TrashStore;

import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.prac.framework.util.Constants;
import com.prac.framework.util.TestNGBase;

/**
 * executable class
 * 
 * @author arvin
 *
 */
public class FlowClass extends TestNGBase {

	WebDriver driver = null;

	@Test
	public void validCaseWOPram() {
		// few reporting must
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			logInfo(testName, "VAL1", "Browser started");
			Thread.sleep(15000);
			log(testName, "Navigation", "Successs Expected", "Success found", Constants.Reporting.PASS);
		} catch (Exception e) {
			log(testName, "Error found", "No exception expected", "exception found: " + e.getMessage(),
					Constants.Reporting.FAIL);
		}
	}

	@DataProvider(name = "dataProvider", parallel = true)
	public Object[][] dataProvider() {
		return businessFunction.getDataFromExcel("Google");
	}

	@Test(dataProvider = "dataProvider")
	public void ExcelPram(Object[] testData) {
		// few reporting must
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName()) + "." + testData[0];
		try {
			logInfo(testName, "VAL1", "Browser started");
			Thread.sleep(15000);
			log(testName, "Navigation", "Successs Expected", "Success found", Constants.Reporting.PASS);
		} catch (Exception e) {
			log(testName, "Error found", "No exception expected", "exception found: " + e.getMessage(),
					Constants.Reporting.FAIL);
		}
	}

	@DataProvider(name = "dataProvider1", parallel = true)
	public Object[][] dataProvider1() {
		Object[][] data = { { "TCX1", "Wow" }, { "TCX2", "Wowx" } };
		return data;
	}

	@Test(dataProvider = "dataProvider1")
	public void datPram(Object... args) {
		// few reporting must
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName()) + "." + args[0];
		try {
			logInfo(testName, "VAL1", "INFO-LOG");
			Thread.sleep(5000);
			log(testName, "VAL2", "B", "B");
			log(testName, "VAL3", "A", "A", Constants.Reporting.PASS);
			log(testName, "VAL4", "C", "D", Constants.Reporting.PASS, "https://www.google.com");
		} catch (SkipException e) {
			log(testName, "Skip Exception", "", "", Constants.Reporting.SKIP);
		} catch (Exception e) {
			log(testName, "Error found", "No exception expected", "exception found: " + e.getMessage(),
					Constants.Reporting.FAIL);
		}
	}

	@Test
	public void invalidCaseWithSkip() {
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName());
		// few reporting must
		throw new SkipException("Test Case to be skipped");
	}

	@Test
	public void errorTest() {
		// few reporting must
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			throw new Exception("Some Error");
		} catch (SkipException e) {
			log(testName, "Skip Exception", "", "", Constants.Reporting.SKIP);
		} catch (Exception e) {
			log(testName, "Error found", "No exception expected", "exception found: " + e.getMessage(),
					Constants.Reporting.FAIL);
		}
	}

	@Test(timeOut = 1)
	public void timeoutInterruptedException() {
		// few reporting must
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			Thread.sleep(5000);
			System.out.println("Method passed!");
		} catch (SkipException e) {
			log(testName, "Skip Exception", "", "", Constants.Reporting.SKIP);
		} catch (Exception e) {
			log(testName, "Error found", "No exception expected", "exception found: " + e.getMessage(),
					Constants.Reporting.FAIL);
		}
	}

	@Test(timeOut = 1)
	public void timeoutExceptionMethod() {
		// few reporting must
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {

			for (int i = 0; i < 10; i++) {
				swap(i);
				// something
				log(testName, "TimeOUT timeoutExceptionMethod", "Worked", "", Constants.Reporting.PASS);
			}
		} catch (SkipException e) {
			log(testName, "Skip Exception", "", "", Constants.Reporting.SKIP);
		} catch (Exception e) {
			log(testName, "Error found", "No exception expected", "exception found: " + e.getMessage(),
					Constants.Reporting.FAIL);
		}
	}

	private void swap(int counter) {
		int a = 0, b = 0;
		for (int i = 0; i <= counter; i++) {
			b = a;
			a = i;
			a = b;
		}
	}

	@Test(dataProvider = "dataProvider1", timeOut = (1000 * 10))
	public void timeoutExceptionWP(String tcName, String value) {
		// few reporting must
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName()) + "." + tcName;
		for (int i = 0; i < 1000; i++) {
			log(testName, "TimeOUT Exception", "Worked", "", Constants.Reporting.PASS);
		}
		System.out.println("Method passed!");

	}
}