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

    // 1. تعريف مكان العناصر (Locators) - هنا "مخزن" العناوين
    private By searchBox = By.name("q");
    // هذا يبحث عن أي عنصر يحتوي على سمة data-precision (الخاصة بالأسعار في جوجل)
    private By priceSpan = By.xpath("//span[@data-precision='2'] | //span[@jsname='vW79Id'] | //div[@id='knowledge-finance-wholepage__entity-summary']//span[1]");

    // 2. الـ Constructor: لربط الصفحة بالمتصفح
    public GooglePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // 3. العمليات (Actions): ماذا نستطيع أن نفعل في هذه الصفحة؟
    public void searchFor(String stockName) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(searchBox));
        element.clear();
        element.sendKeys(stockName + " stock price" + Keys.ENTER); // لمستك الذكية هنا أيضاً
    }

    // ميثود تحاكي كتابة البشر (حرف حرف مع تأخير بسيط)
    public void typeSlowly(String text) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(searchBox));
        element.clear();

        // هنا الكود يدمج جملة stock price تلقائياً مع الكلمة القادمة من الإكسل
        String fullSearchText = text + " stock price";

        for (char c : fullSearchText.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            try { Thread.sleep(150); } catch (Exception e) {}
        }
        try { Thread.sleep(1000); } catch (Exception e) {} // انتظر ثانية قبل الضغط على انتر
        element.sendKeys(Keys.ENTER);
    }

    public String getPriceText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(priceSpan)).getText();
        } catch (Exception e) {
            return ""; // حماية إضافية لو لم يجد العنصر لا ينهار التست بل يعيد نص فارغ ويتعامل معه شرط التيست
        }
    }
}