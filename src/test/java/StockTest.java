import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StockTest extends BaseTest {

    @DataProvider(name = "excelStocks", parallel = true)
    public Object[][] getStocksFromExcel() {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/stocks.xlsx";
        return ExcelReader.getExcelData(filePath, "Sheet1");
    }

    @Test(dataProvider = "excelStocks")
    public void checkStockPrices(String stockSearchName) {
        GooglePage google = new GooglePage(getDriver());

        google.typeSlowly(stockSearchName);

        String rawPrice = google.getPriceText();
        if (rawPrice == null || rawPrice.trim().isEmpty()) {
            System.out.println("⚠️ تعذر جلب السعر للسهم: " + stockSearchName);
            Assert.fail("جلب السعر أعطى نتيجة فارغة للسهم: " + stockSearchName);
            return;
        }

        double cleanPrice = cleanPrice(rawPrice);
        System.out.println("✅ " + stockSearchName.trim() + " price is: $" + cleanPrice);

        Assert.assertTrue(cleanPrice > 0, "Stock price should be greater than zero");
    }

    private double cleanPrice(String input) {
        String clean = input.replaceAll("[^0-9.]", "");
        if (clean.isEmpty()) return 0.0;
        return Double.parseDouble(clean);
    }
}