import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class GooglePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // قائمة بجميع الـ Class Names والمحددات المحتملة لبطاقة أسعار الأسهم
    private By[] priceSelectors = new By[]{
            By.cssSelector("span[data-currency-code]"),
            By.cssSelector("span.I3A362"),
            By.cssSelector("span.I65263"),
            By.cssSelector("div.YMlA3e"),
            By.xpath("//div[@class='N261B']//span[contains(text(),'.')]"),
            By.xpath("//span[contains(@class,'D413eb')]")
    };

    public GooglePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void typeSlowly(String stockSymbol) {
        // فتح نتائج بحث جوجل بشكل مباشر وسريع
        String searchUrl = "https://www.google.com/search?q=" + stockSymbol.trim() + "+stock+price&hl=en";
        driver.get(searchUrl);
    }

    public String getPriceText() {
        // تجربة جميع المحددات الممكنة لاستخراج السعر
        for (By selector : priceSelectors) {
            try {
                List<WebElement> elements = driver.findElements(selector);
                for (WebElement el : elements) {
                    String text = el.getText().trim();
                    // التحقق أن النص المستخرج يحتوي على رقم ومبدئياً شكل سعر
                    if (!text.isEmpty() && text.matches(".*\\d+.*")) {
                        return text;
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}