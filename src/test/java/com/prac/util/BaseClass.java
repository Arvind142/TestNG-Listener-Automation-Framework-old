package com.prac.util;

import com.prac.main.BusinessFunction;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

/***
 * BaseClass class consist of few methods calls required for framework to be
 * initialized
 * 
 * @author arvin
 *
 */
public class BaseClass {
	public BusinessFunction businessFunction = new BusinessFunction();

	/***
	 * below method is to initialize required framework level variable for easier
	 * work
	 */
	@BeforeSuite
	public void beforeSuite() {
		businessFunction.readApplicaitonLevelProperty();
		businessFunction.readEnvironmentLevelProperty();
		businessFunction.creatingReportFolder();
	}

	@AfterSuite
	public void afterSuite() {
	}

}
