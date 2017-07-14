import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Iterator;

/**
 * Write data to or read data from an MS Excel File (.xlsx)
 * Excel Format Version 1
 *
 * @author Xuanli Lin
 * @version 0.3.0-alpha
 * @since 0.3.0-alpha
 */
public final class ExcelOp {
    private static final String EXCEL_VER1_HEADER = "Excel Format Version 1";
    private static ObservableList<Key> keys = FXCollections.observableArrayList();

    public static void saveToExcel(File dest, ObservableList<Key> keys) {
        /* Excel format version 1 is similar to the following
         *   -----------------------------------------------------
         * 0 |       Game      |       Key        |    Notes     |
         *   -----------------------------------------------------
         * 1 | Pseudo Game     | 11111-22222-33333|  Some notes  |
         *   -----------------------------------------------------
         * ..|  ...            |  ...             |   ...        |
         *   -----------------------------------------------------
         *         0                    1               2
         */

        // Create a new workbook and a sheet
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Keys");

        // Header row
        Row headerRow = sheet.createRow(0);
        Cell headerRowCell = headerRow.createCell(0);
        headerRowCell.setCellValue("Games");
        headerRowCell = headerRow.createCell(1);
        headerRowCell.setCellValue("Key");
        headerRowCell = headerRow.createCell(2);
        headerRowCell.setCellValue("Notes");

        // Main data body
        int rowNum = 1;
        for (Key key : keys) {
            Row row = sheet.createRow(rowNum++);
            Cell cell = row.createCell(0);
            cell.setCellValue(key.getGame());
            cell = row.createCell(1);
            cell.setCellValue(key.getKey());
            cell = row.createCell(2);
            cell.setCellValue(key.getNotes());
        }

        // Save metainfo to the Excel file
        POIXMLProperties.CoreProperties props = workbook.getProperties().getCoreProperties();
        props.setDescription(EXCEL_VER1_HEADER);

        // Write data to the file
        try {
            FileOutputStream fos = new FileOutputStream(dest.getPath());
            workbook.write(fos);
            workbook.close();
            fos.close();
            ShowPrompt.fileSaved(dest.getPath());
        } catch (FileNotFoundException ex) {
            ShowPrompt.fileReadError(dest.getPath(), 2);    // Since this prompt fits here perfectly...
        } catch (IOException ex) {
            ShowPrompt.fileSaveError(dest.getPath());
        }
    }

    // Excel parser
    public static void parseExcel(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);

            // Obtain the workbook
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            // Check for metainfo. If present then proceed to normal parsing mode
            POIXMLProperties.CoreProperties props = workbook.getProperties().getCoreProperties();
            if (props.getDescription().equals(EXCEL_VER1_HEADER)) {
                parseExcelV1(workbook);
            } else {
                // TODO show prompt and implement old parser
                // Is it really needed?
            }

            // Close the workbook and FileInputStream
            workbook.close();
            fis.close();

        } catch (FileNotFoundException ex) {
            ShowPrompt.fileReadError(file.getPath(), 2);    // Since this prompt fits here perfectly...
        } catch (IOException ex) {
            ShowPrompt.fileSaveError(file.getPath());
        }

    }

    private static void parseExcelV1(XSSFWorkbook workbook) {
        // Get the first (default) sheet of the workbook
        XSSFSheet sheet = workbook.getSheetAt(0);

        // Iterator to iterate through all rows of excel file
        Iterator<Row> rowIterator = sheet.iterator();
        // Omit the first row (header row)
        rowIterator.next();

        // Traverse through each row
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Compile cell data to Key instances
            keys.add(new Key(row.getCell(0).getStringCellValue(),   // Game
                    row.getCell(1).getStringCellValue(),            // Key
                    row.getCell(2).getStringCellValue()));          // Notes
        }
    }

    public static ObservableList<Key> getKeys() {
        return keys;
    }

    public static ObservableList<Key> parseExcelAndGet(File file) {
        parseExcel(file);
        return keys;
    }
}
