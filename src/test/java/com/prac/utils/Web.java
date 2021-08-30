package com.prac.utils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import com.google.common.base.Function;
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
	public synchronized RemoteWebDriver initializeWebDriver(String browserName) {
		RemoteWebDriver driver = null;
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
				throw new InvalidArgumentException("Invalid BrowserName");
			}
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
			driver = null;
			return driver;
		}
		return driver;
	}

	/***
	 * to open any url
	 * 
	 * @param driver web driver variable
	 * @param url    url to open
	 */
	public void openURL(RemoteWebDriver driver, String url) {
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	/**
	 * confirms if page is loaded or not with fluent wait and java script executor
	 * 
	 * @param driver
	 */
	public void waitForPageLoad(RemoteWebDriver driver) {
		FluentWait<RemoteWebDriver> wait = new FluentWait<RemoteWebDriver>(driver).withTimeout(Duration.ofSeconds(20))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Exception.class);
		wait.until(new Function<WebDriver, Boolean>() {
			@Override
			public Boolean apply(WebDriver input) {
				return ((JavascriptExecutor) input).executeScript("return document.readyState").equals("complete");
			}
		});
	}

	/**
	 * to close all browser instances
	 * 
	 * @param driver web driver object
	 */
	public void destroyWebDriver(RemoteWebDriver driver) {
		if (driver != null)
			driver.quit();
	}
}
