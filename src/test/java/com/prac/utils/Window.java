package com.prac.utils;

import java.net.URL;

import org.openqa.selenium.winium.DesktopOptions;
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

	/**
	 * method to start winium driver with serverurl and application path
	 * 
	 * @param serverUrl
	 * @param applicationPath
	 * @return winium driver which script developer can use to work with windows
	 *         automation
	 */
	public WiniumDriver startWiniumDriver(String serverUrl, String applicationPath) {
		DesktopOptions options = new DesktopOptions();
		options.setApplicationPath(applicationPath);
		try {
			return new WiniumDriver(new URL(serverUrl), options);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
