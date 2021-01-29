package com.prac.util;

import com.aventstack.extentreports.Status;

/***
 * Constants class hold constants values it can be related to reporting, DB or
 * UI
 * 
 * @author arvin
 *
 */
public class Constants {
	public static class Reporting {
		public static final Status PASS = Status.PASS;
		public static final Status FAIL = Status.FAIL;
		public static final Status INFO = Status.INFO;
		public static final Status WARNING = Status.WARNING;
		public static final Status SKIP = Status.SKIP;
	}

	public static class WebLocator {
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String XPATH = "xpath";
		public static final String TAGNAME = "tagname";
		public static final String LINKTEXT = "linktext";
	}
}
