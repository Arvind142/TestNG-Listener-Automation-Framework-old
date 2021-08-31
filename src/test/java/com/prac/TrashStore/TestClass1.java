package com.prac.TrashStore;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;
import com.prac.framework.util.TestNGBase;

public class TestClass1 extends TestNGBase {

	@Test(enabled = false)
	public void logPrimitiveComparison() {
		// test case name
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			logInfo(testName, "Execution started", "!!");

			// all comparison

			// INT
			log(testName, "Int Comparison", 12, 12);
			log(testName, "Int Comparison", 124, 12);

			Integer[] expi = { 10, 12, 14 };
			Integer[] acti = { 10, 12, 14 };
			log(testName, "Int[] Comparison", expi, acti);
			Integer[] inacti = { 10, 12, 12 };
			log(testName, "Int[] Comparison", expi, inacti);

			// Long
			log(testName, "Long Comparison", Long.parseLong("1234567890"), Long.parseLong("1234567890"));
			log(testName, "Long Comparison", Long.parseLong("1234567890"), Long.parseLong("12345678901234"));

			Long[] expl = { Long.parseLong("1234567890"), Long.parseLong("1234567890") };
			Long[] actl = { Long.parseLong("1234567890"), Long.parseLong("1234567890") };
			log(testName, "Long[] Comparison", expl, actl);
			Long[] inactl = { Long.parseLong("1234567890"), Long.parseLong("12345678901234") };
			log(testName, "Long[] Comparison", expl, inactl);

			// float
			log(testName, "float Comparison", 12.0f, 12.0f);
			log(testName, "float Comparison", 124.0f, 12.0f);

			Float[] expf = { 12.0f, 12.0f };
			Float[] actf = { 12.0f, 12.0f };
			log(testName, "float[] Comparison", expf, actf);
			Float[] inactf = { 124.0f, 12.0f };
			log(testName, "float[] Comparison", expf, inactf);

			// double
			log(testName, "double Comparison", 14.0d, 14.0d);
			log(testName, "double Comparison", 124.0d, 14.0d);

			Double[] expd = { 12.0d, 12.0d };
			Double[] actd = { 12.0d, 12.0d };
			log(testName, "double[] Comparison", expd, actd);
			Double[] inactd = { 124.0d, 12.0d };
			log(testName, "double[] Comparison", expd, inactd);

			// String
			log(testName, "String Comparison", "ABC", "ABC");
			log(testName, "String Comparison", "ABCD", "ABC");

			String[] exps = { "ABC", "ABC" };
			String[] acts = { "ABC", "ABC" };
			log(testName, "String[] Comparison", exps, acts);
			String[] inacts = { "ABCD", "ABC" };
			log(testName, "String[] Comparison", exps, inacts);

			logInfo(testName, "Execution stopped", "!!");
		} catch (Exception e) {
			e.printStackTrace();
			logError(testName, "execution...", e.getMessage());
		}

	}

	@Test(enabled = false)
	public void seleniumBasedHtmlUnitDriver() {
		// test case name
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			logInfo(testName, "Execution started", "!!");
			HtmlUnitDriver driver = web.initializeHeadlessWebDriver();
			driver.get("http://the-internet.herokuapp.com/abtest");
			System.out.println(web.getWebElement(driver, OR.the_internet.ab.header3, true, 5));
			System.out.println(web.getWebElement(driver, OR.the_internet.ab.header7, false, 5) == null);
			System.out.println(web.getWebElement(driver, OR.the_internet.ab.header9, false, 5));
			Thread.sleep(2000);
			web.destroyWebDriver(driver);
			logInfo(testName, "Execution stopped", "!!");
		} catch (Exception e) {
			e.printStackTrace();
			logError(testName, "execution...", e.getMessage());
		}

	}

	@Test(enabled = true)
	public void seleniumBasedHeadLessUI() {
		// test case name
		String testName = className + "." + (new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			logInfo(testName, "Execution started", "!!");
			// initialize
			EdgeOptions options = new EdgeOptions();
//			options.setHeadless(true);
			WebDriver driver = web.initializeChromiumWebBrowsers("EDGE", options);
			driver.get("http://the-internet.herokuapp.com/abtest");
			// screenshot
			File s = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(s, new File("tutorialpoint.png"));

			// send values to field
			driver.get("http://the-internet.herokuapp.com/login");
			System.out.println(driver.getCurrentUrl());

			web.getWebElement(driver, OR.the_internet.login.username, true, 5)
					.sendKeys(businessFunction.environmentLevelProperty.get("login.username").toString());

			web.getWebElement(driver, OR.the_internet.login.password, true, 5)
					.sendKeys(businessFunction.environmentLevelProperty.get("login.password").toString());

			web.getWebElement(driver, OR.the_internet.login.submitButton, true, 5).click();

			System.out.println(driver.getCurrentUrl());
			
			System.out.println(web.getWebElement(driver, OR.the_internet.secure.header, true, 3).getText());
			// drop down

			// action
			driver.get("http://demo.guru99.com/test/drag_drop.html");

			// Element(BANK) which need to drag.
			WebElement From1 = driver.findElement(By.xpath("//*[@id='credit2']/a"));

			// Element(DEBIT SIDE) on which need to drop.
			WebElement To1 = driver.findElement(By.xpath("//*[@id='bank']/li"));

			// Element(SALES) which need to drag.
			WebElement From2 = driver.findElement(By.xpath("//*[@id='credit1']/a"));

			// Element(CREDIT SIDE) on which need to drop.
			WebElement To2 = driver.findElement(By.xpath("//*[@id='loan']/li"));

			// Element(500) which need to drag.
			WebElement From3 = driver.findElement(By.xpath("//*[@id='fourth']/a"));

			// Element(DEBIT SIDE) on which need to drop.
			WebElement To3 = driver.findElement(By.xpath("//*[@id='amt7']/li"));

			// Element(500) which need to drag.
			WebElement From4 = driver.findElement(By.xpath("//*[@id='fourth']/a"));

			// Element(CREDIT SIDE) on which need to drop.
			WebElement To4 = driver.findElement(By.xpath("//*[@id='amt8']/li"));

			// Using Action class for drag and drop.
			Actions act = new Actions(driver);

			// BANK drag and drop.
			act.dragAndDrop(From1, To1).build().perform();

			// SALES drag and drop.
			act.dragAndDrop(From2, To2).build().perform();

			// 500 drag and drop debit side.
			act.dragAndDrop(From3, To3).build().perform();

			// 500 drag and drop credit side.
			act.dragAndDrop(From4, To4).build().perform();

			// Verifying the Perfect! message.
			if (driver.findElement(By.xpath("//a[contains(text(),'Perfect')]")).isDisplayed()) {
				System.out.println("Perfect Displayed !!!");
			} else {
				System.out.println("Perfect not Displayed !!!");
			}

			Thread.sleep(2000);
			web.destroyWebDriver(driver);
			logInfo(testName, "Execution stopped", "!!");
		} catch (Exception e) {
			e.printStackTrace();
			logError(testName, "execution...", e.getMessage());
		}

	}
}
