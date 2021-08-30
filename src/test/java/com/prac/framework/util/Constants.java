package com.prac.framework.util;

/***
 * Constants class hold constants values it can be related to reporting, DB or
 * UI
 * 
 * @author arvin
 *
 */
public class Constants {
	/**
	 * Reporting class holds all reporting variables
	 * 
	 * @author arvin
	 *
	 */
	public static class Reporting {
		/**
		 * PASS status in log
		 */
		public static final String PASS = "PASS";
		/**
		 * FAIL status in log
		 */
		public static final String FAIL = "FAIL";
		/**
		 * INFO status in log
		 */
		public static final String INFO = "INFO";
		/**
		 * WARNING status in log
		 */
		public static final String WARNING = "WARNING";
		/**
		 * SKIP status in log
		 */
		public static final String SKIP = "SKIP";
	}

	/***
	 * below class holds all web based locators
	 * 
	 * @author arvin
	 *
	 */
	public static class WebLocator {
		/**
		 * locator : id
		 */
		public static final String ID = "id";
		/**
		 * locator : name
		 */
		public static final String NAME = "name";
		/**
		 * locator : xPath
		 */
		public static final String XPATH = "xpath";
		/**
		 * locator : tagName
		 */
		public static final String TAGNAME = "tagname";

		/**
		 * locator : className
		 */
		public static final String CLASS_NAME = "classname";

		/**
		 * locator : cssSelector
		 */
		public static final String CSS_SELECTOR = "cssSelector";
		/**
		 * locator : linkText
		 */
		public static final String LINKTEXT = "linktext";
	}
}
