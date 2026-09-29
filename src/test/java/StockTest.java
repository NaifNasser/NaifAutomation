import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StockTest extends BaseTest {

    @DataProvider(name = "excelStocks", parallel = false)
    public Object[][] getStocksFromExcel() {
        String filePath = "stocks.xlsx";
        return ExcelReader.getExcelData(filePath, "Sheet1");
    }

    @Test(dataProvider = "excelStocks")
    public void checkStockPrices(String stockSearchName) {
        GooglePage google = new GooglePage(getDriver());

        google.openStock(stockSearchName);

        String rawPrice = google.getPriceText();
        if (rawPrice == null || rawPrice.trim().isEmpty()) {
            System.out.println("⚠️ تعذر جلب السعر للسهم: " + stockSearchName);
            Assert.fail("جلب السعر أعطى نتيجة فارغة للسهم: " + stockSearchName);
            return;
        }

        java.math.BigDecimal cleanPrice = PriceParser.parse(rawPrice);
        System.out.println("✅ " + stockSearchName.trim() + " price is: $" + cleanPrice);

        Assert.assertTrue(cleanPrice.signum() > 0, "Stock price should be greater than zero");
    }

}
