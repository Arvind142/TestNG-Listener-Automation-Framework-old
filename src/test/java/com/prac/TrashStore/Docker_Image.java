package com.prac.TrashStore;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;

public class Docker_Image {
	public ChromeOptions chrome = null;
	public WebDriver driver = null;

	@Test
	public void f() {
		try {
			driver.get("https://www.google.com");
			driver.manage().window().maximize();
			Thread.sleep(20000);
			}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@BeforeTest
	@Parameters({ "hubURL" })
	public void beforeTest(@Optional("http://172.27.120.41:4446/wd/hub") String huburl) {
		chrome = new ChromeOptions();
		try {
			driver = new RemoteWebDriver(new URL(huburl), new ChromeOptions());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@AfterTest
	public void afterTest() {
		if(driver!=null) {
			driver.quit();
		}
	}

}
