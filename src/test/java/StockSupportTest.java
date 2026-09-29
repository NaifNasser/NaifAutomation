import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StockSupportTest {
    @DataProvider
    public Object[][] validPrices() {
        return new Object[][] {{"$1,234.56", "1234.56"}, {"129.00 USD", "129.00"},
                {"$0.25", "0.25"}, {" US$ 42.50 ", "42.50"}};
    }

    @Test(dataProvider = "validPrices")
    public void parsesUsdPrices(String input, String expected) {
        Assert.assertEquals(PriceParser.parse(input).toPlainString(), expected);
    }

    @DataProvider
    public Object[][] invalidPrices() {
        return new Object[][] {{""}, {null}, {"-$12.00"}, {"$12.00 +2.0%"},
                {"1.234,56"}, {"1,23"}, {"Loading 123"}};
    }

    @Test(dataProvider = "invalidPrices", expectedExceptions = IllegalArgumentException.class)
    public void rejectsInvalidPrices(String input) {
        PriceParser.parse(input);
    }

    @Test
    public void readsPackagedStocks() {
        Assert.assertTrue(ExcelReader.getExcelData("stocks.xlsx", "Sheet1").length > 0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void missingWorkbookFailsClearly() {
        ExcelReader.getExcelData("missing.xlsx", "Sheet1");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void missingSheetFailsClearly() {
        ExcelReader.getExcelData("stocks.xlsx", "missing");
    }
}
