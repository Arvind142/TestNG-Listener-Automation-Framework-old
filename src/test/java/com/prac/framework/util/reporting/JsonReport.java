package com.prac.framework.util.reporting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.prac.framework.util.ListenerClass;
import com.prac.framework.util.TestLog;

public class JsonReport implements Reporting {
	private JsonObject jsonObject = null;

	public JsonReport() {
		jsonObject = new JsonObject();
	}

	public void writeSuite() {
		Map<String, String> map = ListenerClass.suiteTimeStamps;
		Map<String, Integer> countMap = ListenerClass.suiteExecutionStatus;
		JsonObject suite = new JsonObject();
		map.forEach((k, v) -> suite.addProperty(k, v));
		countMap.forEach((k, v) -> suite.addProperty(k, v));
		jsonObject.add("suite", suite);
	}

	public void writeTestCase() {
		Map<String, List<TestLog>> map = ListenerClass.testResultMap;
		JsonObject testCaseObject = new JsonObject();
		for (String testCase : map.keySet()) {
			JsonArray testCaseObjectArray = new JsonArray();
			testCaseObjectArray.add("Test Status: " + ListenerClass.getTestCaseStatus(map.get(testCase)));
			map.get(testCase).forEach((v) -> testCaseObjectArray.add(v.toString()));
			testCaseObject.add(testCase, testCaseObjectArray);
		}
		jsonObject.add("Test Logs", testCaseObject);
	}

	@Override
	public boolean generateReport(File outputFolder) {
		try {

			// writing suite
			writeSuite();
			// writing cases
			writeTestCase();
			// exporting
			FileWriter file = new FileWriter(outputFolder.getAbsolutePath() + "/result.json");
			file.write(jsonObject.toString());
			file.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
