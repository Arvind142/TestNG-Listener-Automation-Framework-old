package com.prac.util;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import io.github.bonigarcia.wdm.WebDriverManager;

/****
 * Library class hold most basic and required methods to read properties files,
 * initialize framework, logging.Libraray class method should not be modified by testers.
 * 
 * @author arvin
 *
 */
public class Library {

	// property variables
	public Properties applicationLevelProperty = new Properties();
	public Properties environmentLevelProperty = new Properties();

	// environment and path
	public String environmentFolder;
	public String outputFolder;

	// driver related
	public WebDriver driver;

	// reporting
	ExtentTest extentTest;
	ExtentReports extentReports;
	ExtentSparkReporter extentSparkReporter;

	/**
	 * This method will help in reading application level property
	 */
	public void readApplicaitonLevelProperty() {
		try (InputStream ins = getClass().getClassLoader().getResource("ApplicationLevelConfig.properties")
				.openStream()) {
			applicationLevelProperty.load(ins);
			if (applicationLevelProperty.keySet().size() == 0) {
				throw new Exception("Invalid Property file");
			}

			// creating folder if not present
			environmentFolder = "./test-output/" + applicationLevelProperty.get("Environment") + "/";
			if (!new File(environmentFolder).exists())
				new File(environmentFolder).mkdirs();

		} catch (FileNotFoundException e) {
			System.out.println("applicaiton level property file missing!");

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/***
	 * readEnvironmentLevelProperty is used to read environment properties. each
	 * environment have different url's and connection details as per need so
	 * categorized. this methods read and stores value in environment variable
	 */
	public void readEnvironmentLevelProperty() {
		try (InputStream ins = getClass().getClassLoader()
				.getResource(applicationLevelProperty.getProperty("Environment") + "/"
						+ applicationLevelProperty.getProperty("ApplicationName") + ".properties")
				.openStream()) {
			environmentLevelProperty.load(ins);
			if (environmentLevelProperty.keySet().size() == 0) {
				throw new Exception("Invalid Property file");
			}
		} catch (FileNotFoundException e) {
			System.out.println("applicaiton level property file missing!");

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/***
	 * this method lets you open webdriver, it slect webbrowser based on given value
	 * in applicaiton.properties... it can open CHROME,EDGE,IE and FIREFOX browser
	 */
	public void initializeWebDriver() {
		try {
			switch (applicationLevelProperty.get("Browser").toString().toUpperCase()) {
			case "CHROME":
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();
				break;
			case "EDGE":
			case "MSEDGE":
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
				break;
			case "FIREFOX":
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
				break;
			case "IE":
			case "INTERNETEXPLORER":
				WebDriverManager.iedriver().setup();
				driver = new InternetExplorerDriver();
				break;
			default:
				throw new Exception("Invalid BrowserName");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * simple and sweet driver.quit to destroy running drivers :D
	 */
	public void destroyWebDriver() {
		if (driver != null)
			driver.quit();
	}

	/**
	 * this method lets you open url whihc is passed as parameter
	 * 
	 * @param url url which user wishes to open
	 */
	public void openURL(String url) {
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	/**
	 * this method creates reporting folder and initialize reporting variables
	 */
	public void creatingReportFolder() {
		outputFolder = environmentFolder + (new File(environmentFolder).listFiles().length + 1) + "";
		new File(outputFolder).mkdir();
		// creating report
		extentSparkReporter = new ExtentSparkReporter(outputFolder + "/index.html");
		extentSparkReporter.config().setEncoding("utf-8");
		extentSparkReporter.config().setDocumentTitle("Execution status");
		extentSparkReporter.config().setReportName("Automation Tets Report");
		extentSparkReporter.config().setTheme(Theme.DARK);

		extentReports = new ExtentReports();
		extentReports.setSystemInfo("Application Level Properties : ", "");
		for (Object key : applicationLevelProperty.keySet())
			extentReports.setSystemInfo(key.toString(), applicationLevelProperty.get(key).toString());
		extentReports.setSystemInfo("Environment Level Properties : ", "");
		for (Object key : environmentLevelProperty.keySet())
			extentReports.setSystemInfo(key.toString(), environmentLevelProperty.get(key).toString());
		extentReports.attachReporter(extentSparkReporter);
	}

	// below code is for specific tc reporting
	/***
	 * this method will create report for specific test initiation
	 * 
	 * @param className current we are using classname as test name sooon will
	 *                  change
	 */
	public void startTestCaseReporting(String className) {
		extentTest = extentReports.createTest(className);
	}

	/***
	 * logging for info statements
	 * 
	 * @param stepDescription step description of log
	 */
	public void logResult(String stepDescription) {
		extentTest.log(Status.INFO, stepDescription);
	}

	/***
	 * this method lets code decides if this log statements should be considered as
	 * pass/fail based on expected and actual value
	 * 
	 * @param StepDesciption description of log
	 * @param expected       expected result
	 * @param actual         actual result
	 */
	public void logResult(String StepDesciption, String expected, String actual) {
		extentTest.log(expected.equalsIgnoreCase(actual) ? Status.PASS : Status.FAIL,
				StepDesciption + " expected: " + expected + " & actual: " + actual);
	}

	/**
	 * this methods lets user decide if log is pass/fail and pass that same status
	 * as parameter in this method
	 * 
	 * @param StepDesciption description of step
	 * @param expected       expected value
	 * @param actual         actual value
	 * @param status         status of log to be reported
	 */
	public void logResult(String StepDesciption, String expected, String actual, Status status) {
		extentTest.log(status, StepDesciption + "\t || expected: " + expected + " & actual: " + actual);
	}

	/***
	 * this methods lets extentReporter know that we can close reporting for test
	 */
	public void stopTestCaseReporting() {
		extentReports.flush();
	}

	/***
	 * getDataFromExcel retrieve testdata from excel and return it in Object[][]
	 * fillo Libraries are used to get data from excel required parameter is
	 * className which would select all cases from givenSheet, sheet name should be
	 * same as class name. to categorize cases and leads to simplicity in
	 * identification of cases and testData and script maintenance
	 * 
	 * @param className required parameter is className
	 * @return Object[][] which holds testdata
	 */
	public Object[][] getDataFromExcel(String className) {
		Object[][] excelData = null;
		try {
			Recordset recordSet = new Fillo()
					.getConnection(getClass().getClassLoader()
							.getResource(applicationLevelProperty.getProperty("Environment") + "/"
									+ applicationLevelProperty.getProperty("ApplicationName") + ".xlsx")
							.getPath())
					.executeQuery("SELECT * FROM " + className + " where Execution_FLag='Y'");
			excelData = new Object[recordSet.getCount()][recordSet.getFieldNames().size()];
			List<String> columns = recordSet.getFieldNames();
			Integer colLocator = 0, rowLocator = 0;
			while (recordSet.next()) {
				colLocator = 0;
				for (String column : columns) {
					excelData[rowLocator][colLocator] = recordSet.getField(column);
					colLocator++;
				}
				rowLocator++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return excelData;
	}

}