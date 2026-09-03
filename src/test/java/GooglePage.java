import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class GooglePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By searchBox = By.name("q");

    // أزرار موافقة الكوكيز باللغتين الإنجليزية والعربية
    private By acceptCookiesButtons = By.xpath("//button[contains(., 'Accept all') or contains(., 'I agree') or contains(., 'الموافقة على الكل') or contains(., 'أوافق')]");

    // عناصر سعر السهم المتعددة في Google Finance Card
    private By priceLocators = By.xpath("//span[@class='I3A362'] | //span[@class='I65263'] | //div[@class='YMlA3e'] | //span[contains(@data-value, '.')] | //span[@class='I8A362']");

    public GooglePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        bypassCookieConsent();
    }

    private void bypassCookieConsent() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement acceptBtn = shortWait.until(ExpectedConditions.elementToBeClickable(acceptCookiesButtons));
            acceptBtn.click();
        } catch (Exception ignored) {
            // إذا لم يظهر بنر الكوكيز يتجاوز الخطوة فوراً
        }
    }

    public void typeSlowly(String text) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(searchBox));
        element.clear();
        element.sendKeys(text + Keys.ENTER);
    }

    public String getPriceText() {
        try {
            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(priceLocators));
            return priceElement.getText();
        } catch (Exception e) {
            return null;
        }
    }
}