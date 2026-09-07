import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class GooglePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // المحدد الخاص بسعر السهم في Google Finance
    private By googleFinancePrice = By.xpath("//div[@class='YMlA3e'] | //span[@class='I3A362'] | //div[contains(@class,'fx33pd')]");

    public GooglePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void typeSlowly(String stockSymbol) {
        // الانتقال المباشر لصفحة السهم في Google Finance لتفادي البحث والكوكيز
        String symbol = stockSymbol.trim().toUpperCase();
        String url = "https://www.google.com/finance/quote/" + symbol + ":NASDAQ";
        driver.get(url);
    }

    public String getPriceText() {
        try {
            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(googleFinancePrice));
            return priceElement.getText();
        } catch (Exception e) {
            // محاولة إضافية للبحث عبر البورصة الأخرى (NYSE) في حال لم يكن NASDAQ
            try {
                String currentUrl = driver.getCurrentUrl();
                if (currentUrl.contains("NASDAQ")) {
                    driver.get(currentUrl.replace("NASDAQ", "NYSE"));
                    WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(googleFinancePrice));
                    return priceElement.getText();
                }
            } catch (Exception ignored) {}
            return null;
        }
    }
}