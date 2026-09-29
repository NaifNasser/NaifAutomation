import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.time.Duration;

@Listeners({AllureTestNg.class, TestListener.class})
public class BaseTest {
    // استخدام ThreadLocal لضمان عزلة المتصفح لكل Thread
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

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

        WebDriver driver = new ChromeDriver(options);

        // إعداد أوقات الانتظار لتفادي فشل تحميل الصفحات البطيئة
        driverThreadLocal.set(driver);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

    }

    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    @AfterMethod(alwaysRun = true)
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