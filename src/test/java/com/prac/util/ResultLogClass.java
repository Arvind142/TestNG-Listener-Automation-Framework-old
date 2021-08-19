package com.prac.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * reported Class which handles all reporting
 * 
 * @author arvin
 *
 */
public class ResultLogClass {
	public static Map<String, List<String>> testResultMap = new ConcurrentHashMap<String, List<String>>();
}
