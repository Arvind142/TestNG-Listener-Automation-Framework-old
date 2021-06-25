package com.prac.util;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.google.common.base.Function;

import io.github.bonigarcia.wdm.WebDriverManager;

/****
 * Library class hold most basic and required methods to read properties files,
 * initialize framework, logging.Libraray class method should not be modified by
 * testers.
 * 
 * @author arvin
 *
 */
public class Library {

	// property variables
	public final static Properties applicationLevelProperty = new Properties();
	public final static Properties environmentLevelProperty = new Properties();

	// environment and path
	public static String environmentFolder;
	public static String outputFolder;

	// driver related
	public WebDriver driver;

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
			creatingReportFolder();
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
			System.out.println("environment level property file missing!");

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
		System.setProperty("log", outputFolder);

	}

	/***
	 * logging for info statements
	 * 
	 * @param stepDescription step description of log
	 */
	public void logResult(String stepDescription) {
		ReporterClass.extentTest.log(Status.INFO, stepDescription);
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
		ReporterClass.extentTest.log(expected.equalsIgnoreCase(actual) ? Status.PASS : Status.FAIL,
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
		ReporterClass.extentTest.log(status, StepDesciption + "\t || expected: " + expected + " & actual: " + actual);
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

	/***
	 * getMySQLDBConnection method can be used to get connection established to
	 * mySQL DB
	 * 
	 * @param host     dbhost
	 * @param username dbusername
	 * @param password dbpassword
	 * @return return connection of java.sql.connection type when connection is
	 *         successful or else null
	 */
	public Connection getDBConnection(String dataBaseName, String host, String port, String service, String username,
			String password) {
		try {
			switch (dataBaseName.toLowerCase()) {
			case "mysql":
				Class.forName("com.mysql.cj.jdbc.Driver");
				return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + service, username,
						password);
			case "oracle":
				Class.forName("oracle.jdbc.driver.OracleDriver");
				return DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":" + port + ":" + service, username,
						password);
			default:
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/***
	 * below method would return by class object with locator populated
	 * @param locator
	 * @return by Object
	 */
	public By by(String locator) {
		String identifier=locator.split("~")[0];
		switch(identifier){
			case Constants.WebLocator.ID:
				return By.id(locator.split("~")[1]);
			case Constants.WebLocator.NAME:
				return By.name(locator.split("~")[1]);
			case Constants.WebLocator.XPATH:
				return By.xpath(locator.split("~")[1]);
			case Constants.WebLocator.TAGNAME:
				return By.tagName(locator.split("~")[1]);
			case Constants.WebLocator.LINKTEXT:
				return By.linkText(locator.split("~")[1]);
			default:
				return null;
		}
	}

	
	/***
	 * below method follows fluent wait mechanism 
	 * @param driver
	 * @param locator
	 * @param exception
	 * @param timeout
	 * @return will return webelement or null based on method execution 
	 */
	public WebElement getWebElement(WebDriver driver, String locator, Integer timeout) {
		
		//fluent Wait declaration
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeout))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);
		
		//use of FluentWait
		return wait.until(new Function<WebDriver, WebElement>() {
			
			//implementing interface method
			@Override
			public WebElement apply(WebDriver driver) {
				//returning value
				//if element not found null will be returned and it will check again till timeout duration is not reached
				return driver.findElement(by(locator));
			}
		});

	}

}