package com.prac.util;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class HTMLReporting {
	HashMap<String, List<String>> testResults = null;
	String baseHtmlPath = "./HTMLReportFormat/Index.html";
	String baseHtmlFolder = "./HTMLReportFormat/";
	String baseCssFolder = "./HTMLReportFormat/css/";

	// reporting
	String testLinks = "<a class=\"nav-link\" id=\"v-pills-${testCaseName}-tab\" data-toggle=\"pill\"	href=\"#v-pills-${testCaseName}\" role=\"tab\" aria-controls=\"v-pills-${testCaseName}\" aria-selected=\"true\">${testCaseName}</a>";

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
			reportString = reportString.replace("${totalCase}", String.valueOf((context.getPassedTests().size()
					+ context.getFailedTests().size() + context.getSkippedTests().size())));
			reportString = reportString.replace("${passed}", String.valueOf(context.getPassedTests().size()));
			reportString = reportString.replace("${failed}", String.valueOf(context.getFailedTests().size()));
			reportString = reportString.replace("${skipped}", String.valueOf(context.getSkippedTests().size()));

			// adding test cases hyperlink
			reportString = reportString.replace("${testCaseLinks}", getAllTestCasesLink(testLinks));
			reportString = reportString.replace("${testTables}", getAllTestTables());
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
		for (String key : ResultLogClass.testResultMap.keySet()) {
			testCaseLinks += (linkFormat.replace("${testCaseName}", key));
		}
		return testCaseLinks;
	}

	/***
	 * get tables created out of logs
	 * 
	 * @param linkFormat
	 * @return
	 */
	public String getAllTestTables() {
		String upperFormat = "<div class=\"tab-pane fade show\"\r\n" + "     id=\"v-pills-${testCaseName}\"\r\n"
				+ "     role=\"tabpanel\"\r\n" + "     aria-labelledby=\"v-pills-${testCaseName}-tab\">\r\n"
				+ "	<table class=\"table table-striped\">\r\n" + "		<thead>\r\n" + "			<tr>\r\n"
				+ "				<th scope=\"col\">#</th>\r\n" + "				<th scope=\"col\">Step</th>\r\n"
				+ "				<th scope=\"col\">Expected</th>\r\n"
				+ "				<th scope=\"col\">Actual</th>\r\n" + "				<th scope=\"col\">Status</th>\r\n"
				+ "				<th scope=\"col\">Evidence</th>\r\n" + "			</tr>\r\n" + "		</thead>\r\n"
				+ "		<tbody>";
		String belowFormat = "</tbody>\r\n" + "	</table>\r\n" + "</div>";
		String testCaseLinks = "";
		for (String key : ResultLogClass.testResultMap.keySet()) {
			testCaseLinks += (upperFormat.replace("${testCaseName}", key)
					+ getRowsCreated(ResultLogClass.testResultMap.get(key)) + belowFormat);
		}
		return testCaseLinks;
	}

	/***
	 * create and return table rows
	 * 
	 * @return
	 */
	public String getRowsCreated(List<String> log) {
		String rowFormat = "<tr><td>${stepNo}</td><td>${stepName}</td><td>${expected}</td><td>${actual}</td><td>${status}</td><td><a href=\"${link}\" >ClickHere!</a></td></tr>";
		String rows = "", row = "";
		int counter = 1;
		for (String record : log) {
			row = "";
			String[] content = record.split(Constants.Reporting.seprator);
			switch (content.length) {
			case 5:
				row = (rowFormat.replace("${stepNo}", String.valueOf(counter))).replace("${stepName}", content[0])
						.replace("${expected}", content[1]).replace("${actual}", content[2])
						.replace("${status}", content[3]).replace("${link}", content[4]);
				break;
			case 4:
				row = (rowFormat.replace("${stepNo}", String.valueOf(counter))).replace("${stepName}", content[0])
						.replace("${expected}", content[1]).replace("${actual}", content[2])
						.replace("${status}", content[3]).replace("<a href=\"${link}\" >ClickHere!</a>", "");
				break;

			default:
				break;
			}
			rows += row;
			counter++;
		}
		return rows;
	}
}
