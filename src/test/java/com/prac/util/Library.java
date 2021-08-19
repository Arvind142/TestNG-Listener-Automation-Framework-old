package com.prac.util;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
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
	public final Properties applicationLevelProperty = new Properties();
	public final Properties environmentLevelProperty = new Properties();

	// environment and path
	public String environmentFolder;
	public String outputFolder;

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
	public synchronized WebDriver initializeWebDriver(String browserName) {
		WebDriver driver = null;
		try {
			switch (browserName.toUpperCase()) {
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
		return driver;
	}

	/***
	 * simple and sweet driver.quit to destroy running drivers :D
	 */
	public void destroyWebDriver(WebDriver driver) {
		if (driver != null)
			driver.quit();
	}

	/**
	 * this method lets you open url whihc is passed as parameter
	 * 
	 * @param url url which user wishes to open
	 */
	public void openURL(WebDriver driver, String url) {
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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
	public Connection getMySqlDBConnection(String host, String username, String password) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://" + host, username, password);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}