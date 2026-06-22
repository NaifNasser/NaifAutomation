import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;
import java.util.Collections;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class FirstTest {
    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", java.util.Collections.singletonList("enable-automation"));
        // 1. تشغيل محرك متصفح كروم
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        // 2. تكبير نافذة المتصفح
        driver.manage().window().maximize();
        try {
            driver.get("https://www.google.com");
            WebElement searchBox = driver.findElement(By.name("q"));
            searchBox.sendKeys("CRWV stock price"+ Keys.ENTER);
            WebElement priceElement = driver.findElement(By.xpath("//span[@jsname='vW79Id'] | //span[@data-precision='2'] | //div[@data-attrid='Price']//span"));
            // انتظر حتى يظهر النص داخل العنصر (بحد أقصى 10 ثواني)
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(priceElement));
            double priceValue = 0.0;
            String stockPrice = priceElement.getText();
            String cleanPrice = stockPrice.replaceAll("[^0-9٠-٩.٫]", "");
            cleanPrice = cleanPrice
                    .replace('٠', '0').replace('١', '1').replace('٢', '2')
                    .replace('٣', '3').replace('٤', '4').replace('٥', '5')
                    .replace('٦', '6').replace('٧', '7').replace('٨', '8')
                    .replace('٩', '9').replace('٫', '.');
            if (cleanPrice.indexOf('.') != cleanPrice.lastIndexOf('.')) {
                int firstPoint = cleanPrice.indexOf('.');
                cleanPrice = cleanPrice.substring(0, firstPoint+3);
            }
            if (!cleanPrice.isEmpty()) {
                priceValue = Double.parseDouble(cleanPrice);
                System.out.println("✅ تم التحويل بنجاح: " + priceValue);
            } else {
                System.out.println("⚠️ لا يزال النص فارغاً بعد التنظيف!");
            }
            String currency = driver.findElement(By.xpath("//span[contains(text(),'USD')]")).getText();
            System.out.println("------------------");
            System.out.println("Current price is: " + priceValue + " " + currency.split(" ")[0]);
            double targetPrice = 129.00;

            if(priceValue > targetPrice) {
                System.out.println("Current price is greater than Target price");
            } else {
                System.err.println("Current price is less than Target price");
            }
            System.out.println("-----------------");

        } catch (Exception e) {
            System.out.println("ooops");
            e.printStackTrace();
        }

        driver.quit();
    }
}