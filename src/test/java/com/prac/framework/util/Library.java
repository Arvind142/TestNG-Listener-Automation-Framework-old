package com.prac.framework.util;

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
				.getResource(applicationLevelProperty.getProperty("Environment") + "/"+ applicationLevelProperty.getProperty("ApplicationName") + ".properties")
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