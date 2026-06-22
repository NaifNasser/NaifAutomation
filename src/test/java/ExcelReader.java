import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    public static Object[][] getExcelData(String filePath, String sheetName) {
        List<String> dataList = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet(sheetName);
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
            System.out.println("❌ فشل في قراءة ملف الإكسل!");
            e.printStackTrace();
        }

        // تحويل القائمة إلى Array تناسب الـ DataProvider الخاص بـ TestNG
        Object[][] data = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i);
        }
        return data;
    }
}