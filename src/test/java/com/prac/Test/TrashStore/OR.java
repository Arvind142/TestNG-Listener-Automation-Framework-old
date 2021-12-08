package com.prac.Test.TrashStore;

import com.prac.framework.util.Constants.WebLocator;

/**
 * OR class holds locators :)
 * 
 * @author arvin
 *
 */
public class OR {
	/**
	 * to hold element identifiers of http://the-internet.herokuapp.com
	 * 
	 * @author arvin
	 *
	 */
	public static class the_internet {

		/**
		 * class holds ab page element identifiers
		 * 
		 * @author arvin
		 *
		 */
		public static class ab {
			public static final String header3 = WebLocator.XPATH + "~" + "//h3";
			public static final String header7 = WebLocator.XPATH + "~" + "//h7";
			public static final String header9 = WebLocator.XPATH + "~" + "//h9";
		}

		/**
		 * class holds login page element identifiers
		 * 
		 * @author arvin
		 *
		 */
		public static class login {
			public static final String username = WebLocator.ID + "~" + "username";
			public static final String password = WebLocator.ID + "~" + "password";
			public static final String submitButton = WebLocator.XPATH + "~" + "//button[@type='submit']";
		}

		/**
		 * class holds secure page element identifiers
		 * 
		 * @author arvin
		 *
		 */
		public static class secure {
			public static final String header = WebLocator.XPATH + "~" + "//h4";
		}

	}

	public static class guru99 {
		public static class test {
			public static class drag_and_drop {
				public static final String bank = WebLocator.XPATH + "~" + "//*[@id='bank']/li";
				public static final String credit1 = WebLocator.XPATH + "~" + "//*[@id='credit1']/a";
				public static final String credit2 = WebLocator.XPATH + "~" + "//*[@id='credit2']/a";
				public static final String loan = WebLocator.XPATH + "~" + "//*[@id='loan']/li";
				public static final String fourth = WebLocator.XPATH + "~" + "//*[@id='fourth']/a";
				public static final String amt7 = WebLocator.XPATH + "~" + "//*[@id='amt7']/li";
				public static final String amt8 = WebLocator.XPATH + "~" + "//*[@id='amt8']/li";
				public static final String perfectHyperLink = WebLocator.XPATH + "~"
						+ "//a[contains(text(),'Perfect')]";
			}
		}
	}
}
