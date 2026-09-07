import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.time.Duration;
import java.util.Collections;

@Listeners({AllureTestNg.class, TestListener.class})
public class BaseTest {
    // استخدام ThreadLocal لضمان عزلة المتصفح لكل Thread
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        // خيارات تشغيل المتصفح في السحابة (GitHub Actions / Linux)
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--lang=en-US"); // توحيد لغة المتصفح لتفادي تغير HTML

        // التخفي وتجاوز Bot Detection
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        // تعيين User-Agent افتراضي قوي في حال لم يحدد في config.properties
        String userAgent = ConfigReader.getProperty("user.agent");
        if (userAgent == null || userAgent.isEmpty()) {
            userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36";
        }
        options.addArguments("user-agent=" + userAgent);

        WebDriver driver = new ChromeDriver(options);

        // إعداد أوقات الانتظار لتفادي فشل تحميل الصفحات البطيئة
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // تخزين نسخة المتصفح داخل الـ ThreadLocal
        driverThreadLocal.set(driver);

        // ملاحظة: تم إزالة فتح الـ URL التلقائي هنا لكي تفتحه الصفحة المخصصة مباشر
    }

    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    @AfterMethod
    public void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {
            }
        }
        driverThreadLocal.remove();
    }
}