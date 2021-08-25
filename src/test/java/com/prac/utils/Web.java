package com.prac.utils;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Class to deal with all web related application and operations
 * 
 * @author arvin
 *
 */
public class Web {
	/***
	 * to initialize webdriver based on sent browserName
	 * 
	 * @param browserName browser name for which we want to initialize driver
	 * @return webdriver variable
	 */
	public static synchronized WebDriver initializeWebDriver(String browserName) {
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
	 * to open any url
	 * 
	 * @param driver web driver variable
	 * @param url    url to open
	 */
	public void openURL(WebDriver driver, String url) {
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	/**
	 * to close all browser instances
	 * 
	 * @param driver web driver object
	 */
	public void destroyWebDriver(WebDriver driver) {
		if (driver != null)
			driver.quit();
	}
}
