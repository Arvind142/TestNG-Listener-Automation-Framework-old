package com.prac.framework.util.reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.prac.framework.util.ListenerClass;
import com.prac.framework.util.TestLog;

public class ExcelReport implements Reporting {
	private XSSFWorkbook workbook = null;
	private XSSFSheet testSummary = null;
	private XSSFSheet testDetails = null;
	private CellRangeAddress mergeCells = null;
	private CellStyle defaultStyle = null;
	private CellStyle headerStyle = null;

	// colors for records
	private CellStyle passStyle = null;
	private CellStyle skipStyle = null;
	private CellStyle infoStyle = null;
	private CellStyle failStyle = null;

	private Integer cellWidth = 2757;

	public ExcelReport() {
		workbook = new XSSFWorkbook();
		testSummary = workbook.createSheet("Test_Summary");
		testDetails = workbook.createSheet("Test_Execution_Details");
		defaultStyle = setStyle(defaultStyle, false);
		headerStyle = setStyle(headerStyle, true);
		passStyle = setStyle(headerStyle, "pass");
		skipStyle = setStyle(headerStyle, "skip");
		infoStyle = setStyle(headerStyle, "info");
		failStyle = setStyle(headerStyle, "fail");

	}

	public CellStyle setStyle(CellStyle cell, boolean header) {
		if (header) {
			cell = workbook.createCellStyle();
			cell.setBorderBottom(BorderStyle.MEDIUM);
			cell.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			cell.setBorderRight(BorderStyle.MEDIUM);
			cell.setRightBorderColor(IndexedColors.BLACK.getIndex());
			cell.setBorderTop(BorderStyle.MEDIUM);
			cell.setTopBorderColor(IndexedColors.BLACK.getIndex());
			cell.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
			// and solid fill pattern produces solid grey cell fill
			cell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont font = new XSSFFont();
			font.setBold(true);
			cell.setFont(font);
		} else {
			cell = workbook.createCellStyle();
			cell.setBorderBottom(BorderStyle.THIN);
			cell.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			cell.setBorderRight(BorderStyle.THIN);
			cell.setRightBorderColor(IndexedColors.BLACK.getIndex());
			cell.setBorderTop(BorderStyle.THIN);
			cell.setTopBorderColor(IndexedColors.BLACK.getIndex());
			cell.setWrapText(true);
			cell.setVerticalAlignment(VerticalAlignment.TOP);
			cell.setAlignment(HorizontalAlignment.LEFT);
		}
		return cell;
	}

	public CellStyle setStyle(CellStyle cell, String type) {
		cell = workbook.createCellStyle();
		cell.setBorderBottom(BorderStyle.THIN);
		cell.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		cell.setBorderRight(BorderStyle.THIN);
		cell.setRightBorderColor(IndexedColors.BLACK.getIndex());
		cell.setBorderTop(BorderStyle.THIN);
		cell.setTopBorderColor(IndexedColors.BLACK.getIndex());
		cell.setWrapText(true);
		cell.setVerticalAlignment(VerticalAlignment.TOP);
		cell.setAlignment(HorizontalAlignment.LEFT);

		if (type.equals("pass")) {
			cell.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
			// and solid fill pattern produces solid grey cell fill
			cell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		} else if (type.equals("fail")) {
			cell.setFillForegroundColor(IndexedColors.TAN.index);
			// and solid fill pattern produces solid grey cell fill
			cell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		} else if (type.equals("skip")) {
			cell.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
			// and solid fill pattern produces solid grey cell fill
			cell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		} else if (type.equals("info")) {
			cell.setFillForegroundColor(IndexedColors.LIGHT_BLUE.index);
			// and solid fill pattern produces solid grey cell fill
			cell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}

		return cell;
	}

	public void createSummary() {
		Map<String, String> map = ListenerClass.suiteTimeStamps;
		Map<String, Integer> countMap = ListenerClass.suiteExecutionStatus;
		int counter = 0;
		XSSFRow row = null;

		// create Header row
		testSummary.createRow(counter).createCell(0).setCellValue("Test Summary :)");
		mergeCells = new CellRangeAddress(counter, counter, 0, 1);
		testSummary.addMergedRegion(mergeCells);
		testSummary.getRow(counter).getCell(0).setCellStyle(headerStyle);
		testSummary.getRow(counter).createCell(1).setCellStyle(headerStyle);
		counter++;

		// create empty row
		testSummary.createRow(counter);
		counter++;

		testSummary.createRow(counter).createCell(0).setCellValue("Test Execution TimeStamp:");
		mergeCells = new CellRangeAddress(counter, counter, 0, 1);
		testSummary.addMergedRegion(mergeCells);
		testSummary.getRow(counter).getCell(0).setCellStyle(headerStyle);
		testSummary.getRow(counter).createCell(1).setCellStyle(headerStyle);
		counter++;

		for (String key : map.keySet()) {
			row = testSummary.createRow(counter);
			row.createCell(0).setCellValue(key);
			row.createCell(1).setCellValue(map.get(key));

			testSummary.getRow(counter).getCell(0).setCellStyle(defaultStyle);
			testSummary.getRow(counter).getCell(1).setCellStyle(defaultStyle);
			counter++;
		}

		// create empty row
		testSummary.createRow(counter);
		counter++;

		testSummary.createRow(counter).createCell(0).setCellValue("Test Execution Details:");
		mergeCells = new CellRangeAddress(counter, counter, 0, 1);
		testSummary.addMergedRegion(mergeCells);
		testSummary.getRow(counter).getCell(0).setCellStyle(headerStyle);
		testSummary.getRow(counter).createCell(1).setCellStyle(headerStyle);
		counter++;
		for (String key : countMap.keySet()) {
			row = testSummary.createRow(counter);
			row.createCell(0).setCellValue(key);
			row.createCell(1).setCellValue(countMap.get(key));

			testSummary.getRow(counter).getCell(0).setCellStyle(defaultStyle);
			testSummary.getRow(counter).getCell(1).setCellStyle(defaultStyle);
			counter++;
		}

		testSummary.setColumnWidth(0, cellWidth * 2);
		testSummary.setColumnWidth(1, cellWidth * 2);

	}

	public void createTestDetails() {
		XSSFRow currentRow = null;
		int rowCounter = 0;
		currentRow = testDetails.createRow(rowCounter);
		currentRow.createCell(0).setCellValue("Sr.No");
		currentRow.createCell(1).setCellValue("TestCase_Name");
		currentRow.createCell(2).setCellValue("Step Description");
		currentRow.createCell(3).setCellValue("Expected");
		currentRow.createCell(4).setCellValue("Actual");
		currentRow.createCell(5).setCellValue("Status");
		currentRow.createCell(6).setCellValue("Evidence");
		currentRow.createCell(7).setCellValue("TimeStamp");
		// formatting

		currentRow.getCell(0).setCellStyle(headerStyle);
		currentRow.getCell(1).setCellStyle(headerStyle);
		currentRow.getCell(2).setCellStyle(headerStyle);
		currentRow.getCell(3).setCellStyle(headerStyle);
		currentRow.getCell(4).setCellStyle(headerStyle);
		currentRow.getCell(5).setCellStyle(headerStyle);
		currentRow.getCell(6).setCellStyle(headerStyle);
		currentRow.getCell(7).setCellStyle(headerStyle);

		rowCounter++;
		int counter = 1;
		int startIndex = 0;
		int endIndex = 0;
		Map<String, List<TestLog>> map = ListenerClass.testResultMap;
		for (String tc : map.keySet()) {
			startIndex = rowCounter;
			CellStyle style = null;
			switch (ListenerClass.getTestCaseStatus(map.get(tc))) {

			case "PASS":
				style = passStyle;
				break;

			case "FAIL":
				style = failStyle;
				break;

			case "INFO":
				style = infoStyle;
				break;

			case "SKIP":
				style = skipStyle;
				break;

			default:
				style = defaultStyle;
				break;
			}
			for (TestLog log : map.get(tc)) {
				currentRow = testDetails.createRow(rowCounter);

				// data Setup
				currentRow.createCell(0).setCellValue(counter);
				currentRow.createCell(1).setCellValue(tc);
				currentRow.createCell(2).setCellValue(log.getStepDescription());
				currentRow.createCell(3).setCellValue(log.getExpectedValue());
				currentRow.createCell(4).setCellValue(log.getActualValue());
				currentRow.createCell(5).setCellValue(log.getLogStatus());
				currentRow.createCell(6).setCellValue(log.getAttachment());
				currentRow.createCell(7).setCellValue(log.getEvidenceTime());

				currentRow.getCell(0).setCellStyle(style);
				currentRow.getCell(1).setCellStyle(style);
				currentRow.getCell(2).setCellStyle(style);
				currentRow.getCell(3).setCellStyle(style);
				currentRow.getCell(4).setCellStyle(style);
				currentRow.getCell(5).setCellStyle(style);
				currentRow.getCell(6).setCellStyle(style);
				currentRow.getCell(7).setCellStyle(style);

				rowCounter++;
			}
			endIndex = rowCounter;
			if ((endIndex - startIndex) != 1) {
				mergeCells = new CellRangeAddress(startIndex, endIndex - 1, 0, 0);
				testDetails.addMergedRegion(mergeCells);
				mergeCells = new CellRangeAddress(startIndex, endIndex - 1, 1, 1);
				testDetails.addMergedRegion(mergeCells);
			}
			counter++;
		}

		testDetails.setColumnWidth(0, cellWidth / 2);
		testDetails.setColumnWidth(1, cellWidth * 3);
		testDetails.setColumnWidth(2, cellWidth * 3);
		testDetails.setColumnWidth(3, cellWidth * 3);
		testDetails.setColumnWidth(4, cellWidth * 3);
		testDetails.setColumnWidth(5, (cellWidth / 3) * 2);
		testDetails.setColumnWidth(6, cellWidth * 2);
		testDetails.setColumnWidth(7, cellWidth * 2);
		testDetails.setDefaultRowHeight((short) 600);

	}

	public boolean writeExcel(File outputFolder) {
		try (FileOutputStream outputStream = new FileOutputStream(outputFolder.getPath() + "/result.xlsx")) {
			workbook.write(outputStream);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean generateReport(File outputFolder) {
		boolean flag = false;
		// adding test summary
		createSummary();

		// addign testcases and log Data
		createTestDetails();

		// writing wb to drive
		flag = writeExcel(outputFolder);

		return flag;
	}

}
