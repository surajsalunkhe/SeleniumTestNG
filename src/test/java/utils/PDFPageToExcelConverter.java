package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PDFPageToExcelConverter {
    static Logger log = LogManager.getLogger(PDFPageToExcelConverter.class);
    private static final Object lock = new Object();
    public static void convertPdfPageToExcel(int pageNumber,String fileName) {
        synchronized (lock) {
            String excelFilePath = Constants.OUTPUT_EXCEL_PATH;
            String pdfDirectory = System.getProperty("user.dir") + Constants.REPORT_DOWNLOAD_FOLDER + "/";
            String pdfFilePath = System.getProperty("user.dir") + Constants.REPORT_DOWNLOAD_FOLDER + "/" + fileName;
            boolean fileDownlaoded = isFilePresentInDirectory(pdfDirectory, fileName);
            if (fileDownlaoded == false) {
                try {
                    //minutes to sleep*// seconds to a minute*// milliseconds to a second
                    TimeUnit.SECONDS.sleep(60);
                    //Thread.sleep(3*60*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {
                    // Instantiate PDFTextStripper class
                    PDFTextStripper pdfStripper = new PDFTextStripper();

                    // Set the start and end pages
                    pdfStripper.setStartPage(pageNumber);
                    pdfStripper.setEndPage(pageNumber);

                    // Retrieve text from specified page of PDF document
                    String pdfText = pdfStripper.getText(document);

                    // Create a Workbook and Sheet
                    Workbook workbook = new XSSFWorkbook();
                    Sheet sheet = workbook.createSheet("Page-" + pageNumber);
                    clearSheetData(sheet);
                    // Split the text into lines and write to the Excel sheet
                    String[] lines = pdfText.split("\\r?\\n");
                    int rowNum = 0;
                    for (String line : lines) {
                        Row row = sheet.createRow(rowNum++);
                        Cell cell = row.createCell(0);
                        cell.setCellValue(line);
                    }

                    // Write the workbook to an Excel file
                    try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
                        workbook.write(fileOut);
                    }

                    // Close the workbook
                    workbook.close();

                    log.info("Page " + pageNumber + " of the PDF has been successfully converted to Excel file: " + excelFilePath);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }//end of write lock
    }
    public static void clearSheetData(Sheet sheet){
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                sheet.removeRow(row);
            }
        }
    }

    public static boolean isFilePresentInDirectory(String directoryPath, String fileName) {
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File file = new File(directoryPath, fileName);
            return file.exists() && !file.isDirectory();
        }
        return false;
    }
}
