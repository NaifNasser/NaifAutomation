import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class GooglePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Selector ثابت ومباشر لسعر السهم على Yahoo Finance
    private By stockPriceSelector = By.cssSelector("fin-streamer[data-field='regularMarketPrice']");

    public GooglePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void typeSlowly(String stockSymbol) {
        // الانتقال المباشر لصفحة السهم لمنع مشاكل حجب محركات البحث
        String url = "https://finance.yahoo.com/quote/" + stockSymbol.trim().toUpperCase();
        driver.get(url);
    }

    public String getPriceText() {
        try {
            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(stockPriceSelector));
            return priceElement.getText();
        } catch (Exception e) {
            return null;
        }
    }
}