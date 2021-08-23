package com.prac.util;

import java.util.ArrayList;
import java.util.HashMap;
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

public class HTMLReporting {
	HashMap<String, List<String>> testResults = null;
	String baseHtmlPath = "./HTMLReportFormat/Index.html";
	String baseHtmlFolder = "./HTMLReportFormat/";
	String baseCssFolder = "./HTMLReportFormat/css/";
	// ui date format
	SimpleDateFormat uidateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

	// reporting
	String testLinks = "<a class=\"nav-link \" id=\"v-pills-{testCaseName}-tab\" data-toggle=\"pill\" style=\" color:#000; width:100% \"	href=\"#v-pills-{testCaseName}\" role=\"tab\" aria-controls=\"v-pills-{testCaseName}\" aria-selected=\"true\"><button type=\"button\" class=\"btn btn-{status}\"> </button>&nbsp;{testCaseName}</a>";

	// properties
	Properties applicationLevelProperty = new Properties();

	public void createReport(ITestContext context) {
		// creating report folder
		File reportFolder = createReportFolder();
		// copying all files
		copyFilesIntoDirectory(reportFolder);
		// edit html
		File htmlFile = new File(reportFolder.getAbsolutePath() + "/index.html");
		updateHTML(htmlFile, context);
	}

	/**
	 * copy files into reporting directory
	 * 
	 * @param reportingFolder
	 */
	public void copyFilesIntoDirectory(File reportingFolder) {
		try {
			File htmlFolder = new File(baseHtmlFolder);
			File destFile = null;
			for (File file : htmlFolder.listFiles()) {
				if (file.isDirectory()) {
					for (File subfile : file.listFiles()) {
						destFile = new File(reportingFolder.getAbsolutePath() + "/" + file.getName());
						if (!destFile.exists()) {
							destFile.mkdirs();
						}
						destFile = new File(
								reportingFolder.getAbsolutePath() + "/" + file.getName() + "/" + subfile.getName());
						FileUtils.copyFile(subfile, destFile);
					}
				} else {
					destFile = new File(reportingFolder.getAbsolutePath() + "/" + file.getName());
					FileUtils.copyFile(file, destFile);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			File environmentFolder = new File("./test-output/" + applicationLevelProperty.getProperty("Environment"));
			if (!environmentFolder.exists()) {
				environmentFolder.mkdirs();
			}
			// ::::here we create new reporting folder
			reportFolder = new File(
					environmentFolder.getAbsolutePath() + "/" + (environmentFolder.listFiles().length + 1));
			if (!reportFolder.exists()) {
				reportFolder.mkdirs();
			}
			System.out.println("ReportingFolder: " + reportFolder.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportFolder;

	}

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
			reportString = reportString.replace("{passed}", String.valueOf(context.getPassedTests().size()));
			reportString = reportString.replace("{failed}", String.valueOf(context.getFailedTests().size()));
			reportString = reportString.replace("{skipped}", String.valueOf(context.getSkippedTests().size()));

			// updating executionTime
			reportString = reportString.replace("{startTime}",
					uidateFormat.format(ListenerClass.suiteTimeStamps.get("suiteStartTime")));
			reportString = reportString.replace("{endTime}",
					uidateFormat.format(ListenerClass.suiteTimeStamps.get("suiteEndTime")));
			long diffInMillies = Math.abs(ListenerClass.suiteTimeStamps.get("suiteEndTime").getTime()
					- ListenerClass.suiteTimeStamps.get("suiteStartTime").getTime());
			long hours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			long minutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
			long seconds = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			if (hours != 0) {
				reportString = reportString.replace("{executionTime}", String.valueOf(hours) + " h:"
						+ String.valueOf(minutes) + " m:" + String.valueOf(seconds) + " s");
			} else {
				if (minutes != 0) {
					reportString = reportString.replace("{executionTime}",
							String.valueOf(minutes) + " m:" + String.valueOf(seconds) + " s");
				} else {
					reportString = reportString.replace("{executionTime}", String.valueOf(seconds) + "s");
				}
			}

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
	 * create side panelList of All Test Cases
	 * 
	 * @param context
	 * @param linkFormat
	 * @return
	 */
	public String getAllTestCasesLink(String linkFormat) {
		String testCaseLinks = "";
		for (String key : ListenerClass.testResultMap.keySet()) {
			testCaseLinks += updateHyperLinkeColorPas_fail(linkFormat, key);
		}
		return testCaseLinks;
	}

	/***
	 * add tcName, add color
	 * 
	 * @param link
	 * @return
	 */
	public String updateHyperLinkeColorPas_fail(String link, String key) {
		// tcName updated
		link = (link.replace("{testCaseName}", key.replace(".", "_")));
		// color based on test Status
		for (String resultKey : ListenerClass.testResultMap.keySet()) {
			if (resultKey.endsWith(key)) {
				// getting status
				List<String> logs = ListenerClass.testResultMap.get(resultKey);
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

	public String getTestCaseStatus(List<String> logs) {
		for (String log : logs) {
			if (log.split(Constants.Reporting.seprator)[3].equals(Constants.Reporting.FAIL)) {
				return "danger";
			} else if (log.split(Constants.Reporting.seprator)[3].equals(Constants.Reporting.SKIP)) {
				return "warning";
			}
		}
		return "success";
	}

	/***
	 * get tables created out of logs
	 * 
	 * @param linkFormat
	 * @return
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
	 * update time of Testcase in upperPortion of div
	 * 
	 * @param key
	 * @param str
	 * @return
	 */
	public String testDateUpdate(String key, String str) {
		for (String tc : ListenerClass.testTimeStamps.keySet()) {
			if (tc.endsWith(key)) {
				str = str.replace("{startTime}", uidateFormat.format(ListenerClass.testTimeStamps.get(tc).get(0)));
				str = str.replace("{endTime}", uidateFormat.format(ListenerClass.testTimeStamps.get(tc).get(1)));
			}
		}
		return str;
	}

	/***
	 * create and return table rows
	 * 
	 * @return
	 */
	public String getRowsCreated(List<String> logs) {
		String rowFormat = "<tr><td>${stepNo}</td><td>${stepName}</td><td>${expected}</td><td>${actual}</td><td>${status}</td><td><a href=\"${link}\" target=\"_blank\">ClickHere!</a></td><td>${timestamp}</td></tr>";
		String rows = "", row = "";
		int counter = 1;
		for (String record : logs) {
			row = "";
			String[] content = record.split(Constants.Reporting.seprator);
			if (content[4].equals("[]")) {
				row = (rowFormat.replace("${stepNo}", String.valueOf(counter))).replace("${stepName}", content[0])
						.replace("${expected}", content[1]).replace("${actual}", content[2])
						.replace("${status}", content[3])
						.replace("<a href=\"${link}\" target=\"_blank\">ClickHere!</a>", "")
						.replace("${timestamp}", content[5]);
			} else {
				row = (rowFormat.replace("${stepNo}", String.valueOf(counter))).replace("${stepName}", content[0])
						.replace("${expected}", content[1]).replace("${actual}", content[2])
						.replace("${status}", content[3]).replace("${link}", content[4])
						.replace("${timestamp}", content[5]);
			}
			rows += row;
			counter++;
		}
		return rows;
	}
}
