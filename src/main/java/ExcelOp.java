import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Write data to or read data from an MS Excel File (.xlsx)
 * Excel format version 1
 *
 * @author Xuanli Lin
 * @version 0.3.0-alpha
 * @since 0.3.0-alpha
 */
public final class ExcelOp {
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

        // Write data to the file
        try {
            FileOutputStream fos = new FileOutputStream(dest.getPath());
            workbook.write(fos);
            workbook.close();
            ShowPrompt.fileSaved(dest.getPath());
        } catch (FileNotFoundException ex) {
            // TODO
        } catch (IOException ex) {

        }
    }
}
