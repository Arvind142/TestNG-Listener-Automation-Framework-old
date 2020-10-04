package com.prac;

import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.annotations.BeforeTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterTest;

public class BasicCheck {
	WebDriver driver;

	@Test
	public void f() {
		driver.get("https://www.google.co.in");
		driver.manage().window().maximize();
	}

	@BeforeTest
	public void beforeTest() {
		// setting up driver
		WebDriverManager.edgedriver().setup();
		// initializing driver
		driver = new EdgeDriver();
	}

	@AfterTest
	public void afterTest() {
		driver.close();
	}

}
