package com.prac.framework.util;

import java.util.Calendar;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

/****
 * Library class hold most basic and required methods to read properties files,
 * initialize framework, logging.Libraray class method should not be modified by
 * testers.
 * 
 * @author arvin
 *
 */
public class Library {

	/**
	 * application level property variables :)
	 */
	public Properties applicationLevelProperty = new Properties();
	/**
	 * environment level property variables :)
	 */
	public Properties environmentLevelProperty = new Properties();

	/**
	 * variables to get data from property file
	 */
	public String environmentFolder = ListenerClass.environmentOutputFolder;

	/***
	 * readEnvironmentLevelProperty is used to read environment properties. each
	 * environment have different url's and connection details as per need so
	 * categorized. this methods read and stores value in environment variable
	 */
	public void readEnvironmentLevelProperty() {
		// reading properties from listener class, instead of doing complete process
		// again of locating,reading and folder creation
		applicationLevelProperty = (Properties) ListenerClass.applicationLevelProperty.clone();

		// reading env file
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

	/**
	 * returns current date in string in the format passed
	 * 
	 * @param format format in which you want to see datetime
	 * @return String date format
	 */
	public String getCurrentDate(String format) {
		return (new SimpleDateFormat(format)).format(Calendar.getInstance().getTime());
	}

}