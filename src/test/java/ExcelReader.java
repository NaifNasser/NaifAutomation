import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    private static Workbook openWorkbook(InputStream input, String path) throws IOException {
        if (input == null) throw new IllegalArgumentException("Missing stock resource: " + path);
        return new XSSFWorkbook(input);
    }

    public static Object[][] getExcelData(String filePath, String sheetName) {
        List<String> dataList = new ArrayList<>();
        try (InputStream file = ExcelReader.class.getResourceAsStream("/" + filePath);
             Workbook workbook = openWorkbook(file, filePath)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new IllegalArgumentException("Missing sheet: " + sheetName);
            for (Row row : sheet) {
                Cell cell = row.getCell(0); // يقرأ العمود الأول فقط (A)
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    String cellValue = cell.getStringCellValue().trim();
                    if (!cellValue.isEmpty()) {
                        dataList.add(cellValue);
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read stock data: " + filePath, e);
        }

        // تحويل القائمة إلى Array تناسب الـ DataProvider الخاص بـ TestNG
        if (dataList.isEmpty()) throw new IllegalArgumentException("Stock list is empty: " + filePath);
        Object[][] data = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i);
        }
        return data;
    }
}