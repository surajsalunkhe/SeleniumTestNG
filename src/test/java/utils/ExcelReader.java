package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	private String fileName;
	private String sheetName;
	private int sheetIndex;
	private XSSFWorkbook book;

	private ExcelReader(ExcelReaderBuilder excelReaderBuilder) {
		this.fileName = excelReaderBuilder.fileName;
		this.sheetIndex = excelReaderBuilder.sheetIndex;
		this.sheetName = excelReaderBuilder.sheetName;
	}

	public static class ExcelReaderBuilder {

		private String fileName;
		private String sheetName;
		private int sheetIndex;

		public ExcelReaderBuilder setFileLocation(String location) {
			this.fileName = location;
			return this;
		}

		public ExcelReaderBuilder setSheet(String sheetName) {
			this.sheetName = sheetName;
			return this;
		}

		public ExcelReaderBuilder setSheet(int index) {
			this.sheetIndex = index;
			return this;
		}

		public ExcelReader build() {
			return new ExcelReader(this);
		}

	}

	private XSSFWorkbook getWorkBook(String filePath) throws InvalidFormatException, IOException {
		return new XSSFWorkbook(new File(filePath));
	}

	private XSSFSheet getWorkBookSheet(String fileName, String sheetName) throws InvalidFormatException, IOException {
		this.book = getWorkBook(fileName);
		return this.book.getSheet(sheetName);
	}

	private XSSFSheet getWorkBookSheet(String fileName, int sheetIndex) throws InvalidFormatException, IOException {
		this.book = getWorkBook(fileName);
		return this.book.getSheetAt(sheetIndex);
	}

	public List<List<String>> getSheetData() throws IOException{
		XSSFSheet sheet;
		List<List<String>> outerList = new LinkedList<>();
		
		try {
			sheet = getWorkBookSheet(fileName, sheetName);
			outerList = getSheetData(sheet);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}finally {
			this.book.close();
		}
		return outerList;
	}
	
	public List<List<String>> getSheetDataAt() throws InvalidFormatException, IOException {
		
		XSSFSheet sheet;
		List<List<String>> outerList = new LinkedList<>();
		
		try {
			sheet = getWorkBookSheet(fileName, sheetIndex);
			outerList = getSheetData(sheet);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}finally {
			if(this.book != null)
				this.book.close();
		}
		return outerList;
	}

	private List<List<String>> getSheetData(XSSFSheet sheet) {
		List<List<String>> outerList = new LinkedList<>();
		prepareOutterList(sheet, outerList);
		return Collections.unmodifiableList(outerList);
	}

	private void prepareOutterList(XSSFSheet sheet, List<List<String>> outerList) {
		for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
			List<String> innerList = new LinkedList<>();
			XSSFRow xssfRow = sheet.getRow(i);

			for (int j = xssfRow.getFirstCellNum(); j < xssfRow.getLastCellNum(); j++) {
				prepareInnerList(innerList, xssfRow, j);
			}
			outerList.add(Collections.unmodifiableList(innerList));
		}
	}

	private void prepareInnerList(List<String> innerList, XSSFRow xssfRow, int j) {
		switch (xssfRow.getCell(j).getCellType()) {

		case BLANK:
			innerList.add("");
			break;

		case STRING:
			innerList.add(xssfRow.getCell(j).getStringCellValue());
			break;

		case NUMERIC:
			innerList.add(xssfRow.getCell(j).getNumericCellValue() + "");
			break;

		case BOOLEAN:
			innerList.add(xssfRow.getCell(j).getBooleanCellValue() + "");
			break;

		default:
			throw new IllegalArgumentException("Cannot read the column : " + j);
		}
	}
	public String getCellData(int rowIndex, int colIndex) throws IOException {
		XSSFSheet sheet = null;
		try {
			if (sheetName != null && !sheetName.isEmpty()) {
				sheet = getWorkBookSheet(fileName, sheetName);
			} else {
				sheet = getWorkBookSheet(fileName, sheetIndex);
			}

			if (sheet == null) {
				throw new IllegalArgumentException("Sheet not found.");
			}

			XSSFRow row = sheet.getRow(rowIndex);
			if (row == null) {
				throw new IllegalArgumentException("Row not found.");
			}

			Cell cell = row.getCell(colIndex);
			if (cell == null) {
				return "";
			}

			return getCellValueAsString(cell);
		} catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        } finally {
			if (this.book != null) {
				this.book.close();
			}
		}
	}

	public static String getCellValueAsString(Cell cell) {
		CellType cellType = cell.getCellType();
		switch (cellType) {
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				return String.valueOf(cell.getNumericCellValue());
			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			case BLANK:
				return "";
			case FORMULA:
				return cell.getCellFormula();
			default:
				throw new IllegalArgumentException("Unsupported cell type.");
		}
	}
	public static List<List<String>> readExcelFile(String filePath,String mySheetName) {
		List<List<String>> data = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(filePath);
			 Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheet(mySheetName); // Read the first sheet
			for (Row row : sheet) {
				List<String> rowData = new ArrayList<>();
				for (Cell cell : row) {
					rowData.add(getCellValueAsString(cell));
				}
				data.add(rowData);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	public static List<String> readColumnData(String filePath,String mySheetName, int columnIndex) {
		List<String> columnData = new ArrayList<>();
		//System.out.println("Reading data form Sheet="+mySheetName+"\t and column id="+columnIndex);
		try (FileInputStream fis = new FileInputStream(filePath);
			 Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheet(mySheetName); // Read the first sheet
			for (Row row : sheet) {
				Cell cell = row.getCell(columnIndex);
				if (cell == null || cell.getCellType() == CellType.BLANK) {
					break; // Stop reading when a blank cell is encountered
				}
				columnData.add(getCellValueAsString(cell));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return columnData;
	}
	public static List<String> readRowData(String filePath,String mySheetName, int rowIndex) {
		List<String> rowData = new ArrayList<>();
		//System.out.println("Reading data form Sheet="+mySheetName+"\t and row id="+rowIndex);
		try (FileInputStream fis = new FileInputStream(filePath);
			 Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheet(mySheetName); // Read the first sheet
			Row row = sheet.getRow(rowIndex);
			if (row != null) {
				for (Cell cell : row) {
					rowData.add(getCellValueAsString(cell));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return rowData;
	}
}
