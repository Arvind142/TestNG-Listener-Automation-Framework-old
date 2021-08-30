package com.prac.utils;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.winium.WiniumDriver;

/***
 * Class to deal with windows based automation method
 * 
 * 
 * @author arvin
 *
 */
public class Window {
	/**
	 * driver .exe path
	 */
	private String driverPath = "Drivers/Winium.Desktop.Driver.exe";

	private Process winiumProcess = null;

	private RemoteWebDriver winiumDriver = null;

	/***
	 * method to run winium driver Process
	 * 
	 * @return boolean states for true when driver worked and false when some issue
	 *         Occurred
	 */
	public boolean initializeWiniumDriverExecutable() {
		try {
			winiumProcess = Runtime.getRuntime().exec(driverPath);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/***
	 * method to destroy winium service from back end
	 * 
	 * @return boolean which states true for successful destroy of process and false
	 *         to state some issue
	 * 
	 */
	public boolean destroyWiniumDriverExecutable() {
		try {
			winiumProcess.destroyForcibly();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
