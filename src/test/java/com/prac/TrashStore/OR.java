package com.prac.TrashStore;

import com.prac.framework.util.Constants;

/**
 * OR class holds locators :)
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
	public class the_internet {

		/**
		 * class holds ab page element identifiers
		 * 
		 * @author arvin
		 *
		 */
		public class ab {
			public static final String header3 = Constants.WebLocator.XPATH + "~" + "//h3";
			public static final String header7 = Constants.WebLocator.XPATH + "~" + "//h7";
			public static final String header9 = Constants.WebLocator.XPATH + "~" + "//h9";
		}

		/**
		 * class holds login page element identifiers
		 * 
		 * @author arvin
		 *
		 */
		public class login {
			public static final String username = Constants.WebLocator.ID + "~" + "username";
			public static final String password = Constants.WebLocator.ID + "~" + "password";
			public static final String submitButton = Constants.WebLocator.XPATH + "~" + "//button[@type='submit']";
		}

		/**
		 * class holds secure page element identifiers
		 * 
		 * @author arvin
		 *
		 */
		public class secure {
			public static final String header = Constants.WebLocator.XPATH + "~" + "//h4";
		}

	}
}
