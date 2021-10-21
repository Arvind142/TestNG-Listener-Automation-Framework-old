package com.prac.framework.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonExport {
	private JsonObject jsonObject = null;

	public JsonExport() {
		jsonObject = new JsonObject();
	}

	public void writeJsonSuite(Map<String, String> map,Map<String,Integer> countMap) {
		JsonObject suite=new JsonObject();
		map.forEach((k,v)->suite.addProperty(k, v));
		countMap.forEach((k,v)->suite.addProperty(k, v));
		jsonObject.add("suite", suite);
	}

	public void writeJsonTestCase(Map<String, List<TestLog>> map) {
		JsonObject testCaseObject=new JsonObject();
		for(String testCase:map.keySet()) {
			JsonArray testCaseObjectArray=new JsonArray();
			testCaseObjectArray.add("Test Status: "+ListenerClass.getTestCaseStatus(map.get(testCase)));
			map.get(testCase).forEach((v)->testCaseObjectArray.add(v.toString()));
			testCaseObject.add(testCase, testCaseObjectArray);
		}
		jsonObject.add("Test Logs", testCaseObject);
	}

	public boolean extractJson(File outputLocation) {
		try {
			FileWriter file = new FileWriter(outputLocation.getAbsolutePath() + "/results.json");
			file.write(jsonObject.toString());
			file.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
