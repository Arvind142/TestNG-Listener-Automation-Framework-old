package com.prac.TrashStore;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = {"./src/test/resources/Prod/Features/Demo.feature"}, glue = {
		"com.prac.stepDefinition" })
public class RunnerClass extends AbstractTestNGCucumberTests {

}
