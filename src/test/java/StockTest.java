import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert; // لاحظ استخدام TestNG هنا
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Collections;


public class StockTest extends BaseTest {

    @DataProvider(name = "excelStocks", parallel = true)
    public Object[][] getStocksFromExcel() {
        return ExcelReader.getExcelData("/Users/naif/Desktop/stocks.xlsx","Sheet1");

    }
    @Test(dataProvider = "excelStocks")
    public void checkStockPrices(String stockSearchName) {
        GooglePage google  = new GooglePage(getDriver());
        google.typeSlowly(stockSearchName);

        String rawPrice = google.getPriceText();
        if (rawPrice == null || rawPrice.trim().isEmpty()) {
            System.out.println("⚠️ تعذر جلب السعر للسهم أو الخلية فارغة: " + stockSearchName);
            Assert.fail("جلب السعر أعطى نتيجة فارغة");
            return;
        }
        double cleanPrice = cleanPrice(rawPrice);
        System.out.println("✅" + stockSearchName + " is now: "+ cleanPrice+ "USD");

        Assert.assertNotNull(cleanPrice, "Stock price should not be null");        try { Thread.sleep(2000); } catch (Exception e) {}
    }

    private double cleanPrice(String input) {
        String clean = input.replaceAll("[^0-9٠-٩.٫]", "")
                .replace('٠', '0').replace('١', '1').replace('٢', '2')
                .replace('٣', '3').replace('٤', '4').replace('٥', '5')
                .replace('٦', '6').replace('٧', '7').replace('٨', '8')
                .replace('٩', '9').replace('٫', '.');
        if (clean.indexOf('.') != clean.lastIndexOf('.')) {
            clean = clean.substring(0, clean.lastIndexOf('.'));
        }
        return Double.parseDouble(clean);
    }
}

