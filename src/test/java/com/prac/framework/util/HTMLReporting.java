package com.prac.framework.util;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

/**
 * class to create copy and create html report
 * 
 * @author arvin
 *
 */
public class HTMLReporting {

	// few required vars
	final String baseHtmlPath = "HTMLReportFormat/Index.html";
	final String baseHtmlFolder = "HTMLReportFormat/";

	// ftp details
	FtpFileUpload ftp = null;

	// ui date format
	final SimpleDateFormat uidateFormat = ListenerClass.reportUiDateFormat;

	// reporting
	String testLinks = "<a class=\"nav-link \" id=\"v-pills-{testCaseName}-tab\" data-toggle=\"pill\" style=\" color:#000; width:100% \"	href=\"#v-pills-{testCaseName}\" role=\"tab\" aria-controls=\"v-pills-{testCaseName}\" aria-selected=\"true\"><button type=\"button\" class=\"btn btn-{status}\"> </button>&nbsp;{testCaseName}</a>";

	// properties
	Properties applicationLevelProperty = new Properties();

	/**
	 * to create HTML report
	 * 
	 * @param context ITestContext to help with execution details
	 */
	public void createReport(ITestContext context) {
		// creating report folder
		File reportFolder = createReportFolder();
		// copying all files
		copyFilesIntoDirectory(reportFolder);
		// edit html
		File htmlFile = new File(reportFolder.getAbsolutePath() + "/index.html");
		updateHTML(htmlFile, context);

		ftp = new FtpFileUpload(applicationLevelProperty);
		fileUploadFtp(reportFolder);
	}

	/**
	 * below code will create report folder
	 * 
	 * @return File object pointing to new folder
	 */
	public File createReportFolder() {
		File reportFolder = null;
		try {
			InputStream ins = getClass().getClassLoader().getResource("ApplicationLevelConfig.properties").openStream();
			applicationLevelProperty.load(ins);
			File environmentFolder = new File("test-output/" + applicationLevelProperty.getProperty("Environment"));
			if (!environmentFolder.exists()) {
				environmentFolder.mkdirs();
			}
			// ::::here we create new reporting folder
			reportFolder = new File(environmentFolder.getAbsolutePath() + "/" + (environmentFolder.listFiles().length));
			if (!reportFolder.exists()) {
				reportFolder.mkdirs();
			}
			System.out.println("ReportingFolder: " + reportFolder.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportFolder;

	}

	/**
	 * copy files into reporting directory
	 * 
	 * @param reportingFolder folder where reports should be placed
	 */
	public void copyFilesIntoDirectory(File reportingFolder) {
		try {
			File htmlFolder = new File(baseHtmlFolder);
			File destFile = null;
			for (File file : htmlFolder.listFiles()) {
				destFile = new File(reportingFolder.getAbsolutePath() + "/" + file.getName());
				FileUtils.copyFile(file, destFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * below code will update html file with required details like
	 * passed/failed/skipped and test logs
	 * 
	 * @param file    html file object
	 * @param context ITestContext to help with results
	 */
	public void updateHTML(File file, ITestContext context) {
		try {
			// reading html
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();

			// editing
			String reportString = new String(data, "UTF-8");

			// updating status matrix
			reportString = reportString.replace("{totalCase}", String.valueOf((context.getPassedTests().size()
					+ context.getFailedTests().size() + context.getSkippedTests().size())));
			reportString = reportString.replace("{passed}", String.valueOf(ListenerClass.suiteStatus.get("Passed")));
			reportString = reportString.replace("{failed}", String.valueOf(ListenerClass.suiteStatus.get("Failed")));
			reportString = reportString.replace("{skipped}", String.valueOf(ListenerClass.suiteStatus.get("Skipped")));

			// updating executionTime
			reportString = reportString.replace("{startTime}",
					uidateFormat.format(ListenerClass.suiteTimeStamps.get("suiteStartTime")));
			reportString = reportString.replace("{endTime}",
					uidateFormat.format(ListenerClass.suiteTimeStamps.get("suiteEndTime")));
			reportString = reportString.replace("{executionTime}",
					getTimeDifference(ListenerClass.suiteTimeStamps.get("suiteStartTime"),
							ListenerClass.suiteTimeStamps.get("suiteEndTime")));

			// adding test cases hyperlink
			reportString = reportString.replace("{testCaseLinks}", getAllTestCasesLink(testLinks));
			reportString = reportString.replace("{testTables}", getAllTestTables());

			// writing html
			FileOutputStream out = new FileOutputStream(file);
			data = reportString.getBytes("UTF-8");
			out.write(data);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * to create test case links on left panel
	 * 
	 * @param linkFormat format in which links should be created
	 * @return string which holds hyperlink of in required format
	 */
	public String getAllTestCasesLink(String linkFormat) {
		String testCaseLinks = "";
		for (String key : ListenerClass.testResultMap.keySet()) {
			testCaseLinks += upadateTestStatusInTestLink(linkFormat, key);
		}
		return testCaseLinks;
	}

	/**
	 * to update test case status in hyperlink with required color
	 * 
	 * @param link format of hyperlink
	 * @param key  testCaseName
	 * @return link with correct color appended
	 */
	public String upadateTestStatusInTestLink(String link, String key) {
		// tcName updated
		link = (link.replace("{testCaseName}", key.replace(".", "_")));
		// color based on test Status
		for (String resultKey : ListenerClass.testResultMap.keySet()) {
			if (resultKey.endsWith(key)) {
				// getting status
				List<Test> logs = ListenerClass.testResultMap.get(resultKey);
				switch (getTestCaseStatus(logs)) {
				case "success":
					link = link.replace("{status}", "success");
					break;
				case "danger":
					link = link.replace("{status}", "danger");
					break;
				case "warning":
					link = link.replace("{status}", "warning");
					break;
				default:
					link = link.replace("{status}", "danger");
					break;
				}
				break;
			}
		}
		return link;
	}

	/**
	 * to get test case status based on test logs
	 * 
	 * @param logs test logs
	 * @return color i.e. success - PASS, danger - fail, warning - skip
	 */
	public static String getTestCaseStatus(List<Test> logs) {
		for (Test log : logs) {
			if (log.getLogStatus().equals(Constants.Reporting.FAIL)) {
				return "danger";
			} else if (log.getLogStatus().equals(Constants.Reporting.SKIP)) {
				return "warning";
			}
		}
		return "success";
	}

	/***
	 * to create tables out of testLogs
	 * 
	 * @return String which holds all test case tables in division tab
	 */
	public String getAllTestTables() {
		;
		String upperFormat = "<div class=\"tab-pane fade show\"\r\n" + "     id=\"v-pills-{testCaseName}\"\r\n"
				+ "     role=\"tabpanel\"\r\n" + "     aria-labelledby=\"v-pills-{testCaseName}-tab\">\r\n"
				+ "<div class=\"alert alert-primary\" role=\"alert\">\r\n"
				+ "  start time: {startTime} - end time: {endTime}\r\n" + "</div>"
				+ "	<table class=\"table table-striped table-border table-hover\">\r\n" + "		<thead>\r\n"
				+ "			<tr>\r\n" + "				<th scope=\"col\">#</th>\r\n"
				+ "				<th scope=\"col\">Step</th>\r\n" + "				<th scope=\"col\">Expected</th>\r\n"
				+ "				<th scope=\"col\">Actual</th>\r\n" + "				<th scope=\"col\">Status</th>\r\n"
				+ "				<th scope=\"col\">Evidence</th> <th scope=\"col\">TimeStamp</th>\r\n"
				+ "			</tr>\r\n" + "		</thead>\r\n" + "		<tbody>";
		String belowFormat = "</tbody>\r\n" + "	</table>\r\n" + "</div>";
		String testCaseLinks = "";
		for (String key : ListenerClass.testResultMap.keySet()) {
			testCaseLinks += (upperFormat.replace("{testCaseName}", key.replace(".", "_")).replace(
					"start time: {startTime} - end time: {endTime}",
					testDateUpdate(key, "start time: {startTime} - end time: {endTime}"))
					+ getRowsCreated(ListenerClass.testResultMap.get(key)) + belowFormat);
		}
		return testCaseLinks;
	}

	/**
	 * update time of Test case in upperPortion of division
	 * 
	 * @param key  test case name
	 * @param link is format of link
	 * @return String with date time updated for each test case
	 */
	public String testDateUpdate(String key, String link) {
		Date startTime = null, endTime = null;
		for (String tc : ListenerClass.testTimeStamps.keySet()) {
			if (tc.endsWith(key)) {
				startTime = ListenerClass.testTimeStamps.get(tc).get(0);
				endTime = ListenerClass.testTimeStamps.get(tc).get(1);
				link = link.replace("{startTime}", uidateFormat.format(startTime));
				link = link.replace("{endTime}", uidateFormat.format(endTime));
				link += ", timeTaken: " + getTimeDifference(startTime, endTime);
			}
		}
		return link;
	}

	/***
	 * creates rows for particular test case
	 * 
	 * @param logs test logs
	 * @return String holding table row tags each row representing one test log
	 */
	public String getRowsCreated(List<Test> logs) {
		String rowFormat = "<tr><td>${stepNo}</td><td>${stepName}</td><td>${expected}</td><td>${actual}</td><td>${status}</td><td><a href=\"${link}\" target=\"_blank\">ClickHere!</a></td><td>${timestamp}</td></tr>";
		String rows = "", row = "";
		int counter = 1;
		for (Test log : logs) {
			row = "";
			if (log.getAttachment() == null) {
				row = (rowFormat.replace("${stepNo}", String.valueOf(counter)))
						.replace("${stepName}", log.getStepDescription()).replace("${expected}", log.getExpectedValue())
						.replace("${actual}", log.getActualValue()).replace("${status}", log.getLogStatus())
						.replace("<a href=\"${link}\" target=\"_blank\">ClickHere!</a>", "")
						.replace("${timestamp}", log.getEvidenceTime());
			} else {
				row = (rowFormat.replace("${stepNo}", String.valueOf(counter)))
						.replace("${stepName}", log.getStepDescription()).replace("${expected}", log.getExpectedValue())
						.replace("${actual}", log.getActualValue()).replace("${status}", log.getLogStatus())
						.replace("${link}", log.getAttachment()).replace("${timestamp}", log.getEvidenceTime());
			}
			rows += row;
			counter++;
		}
		return rows;
	}

	/**
	 * below method returns time difference in between of start and end time
	 * 
	 * @param startTime date variable holding start date
	 * @param endTime   date variable holding end date
	 * @return difference can be in *H*M*S or *M*S or *S based on time taken
	 */
	public String getTimeDifference(Date startTime, Date endTime) {
		long diffInMillies = Math.abs(endTime.getTime() - startTime.getTime());
		long hours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		long minutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
		long seconds = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if (hours != 0) {
			return (String.valueOf(hours) + " h:" + String.valueOf(minutes) + " m:" + String.valueOf(seconds) + " s");
		} else {
			if (minutes != 0) {
				return (String.valueOf(minutes) + " m:" + String.valueOf(seconds) + " s");
			} else {
				return (String.valueOf(seconds) + "s");
			}
		}
	}

	public void fileUploadFtp(File reportingFolderpath) {
		try {

			if (!String.valueOf(applicationLevelProperty.get("FTPUpload")).equalsIgnoreCase("yes")) {
				return;
			}

			if (ftp.connect()) {
				if (ftp.login()) {
					if (ftp.createReportDirectory()) {
						if (ftp.fileUpload(reportingFolderpath)) {
							System.out.println("FTP: File upload Passed");

							ftp.close();
						} else {
							System.out.println("FTP: File upload failed");
						}
					} else {
						throw new Exception("reportign directory creation failed");
					}
				} else {
					throw new Exception("login failed");
				}
			} else {
				throw new Exception("Connection refused");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
