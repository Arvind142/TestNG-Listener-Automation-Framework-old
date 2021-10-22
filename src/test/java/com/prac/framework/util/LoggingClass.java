package com.prac.framework.util;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingClass {

	public static Logger log = null;

	private static FileHandler fileHandler = null;

	private static ConsoleHandler consoleHandler = null;

	private LoggingClass(String reportingFolder) {
		consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
		try {
			fileHandler = new FileHandler(reportingFolder + "/log.xml");
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		fileHandler.setLevel(Level.ALL);
		log = Logger.getLogger("ListenerClass");
		log.setUseParentHandlers(false);
		log.setLevel(Level.ALL);
		log.addHandler(fileHandler);
		log.addHandler(consoleHandler);
	}
	
	public static void startLogger(String reportingFolder) {
		new LoggingClass(reportingFolder);
	}
}
