import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import java.util.Collections;

@Listeners({AllureTestNg.class, TestListener.class})
public class BaseTest {
    // استخدام ThreadLocal لضمان أن كل ممر (Thread) له متصفحه الخاص المعزول
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("user-agent=" + ConfigReader.getProperty("user.agent"));

        // حل مشكلة الـ Parallel: ننشئ مجلد بروفايل مؤقت لكل Thread لكي لا تتصادم المتصفحات
        String uniqueProfileDir = ConfigReader.getProperty("chrome.profile.dir") + "_" + Thread.currentThread().getId();
        options.addArguments("--user-data-dir=" + uniqueProfileDir);

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // تخزين نسخة المتصفح داخل الـ ThreadLocal الخاصة بالخيط الحالي
        driverThreadLocal.set(driver);

        getDriver().get(ConfigReader.getProperty("url"));
    }

    // تعديل الـ Getter ليعيد المتصفح الخاص بالـ Thread الحالي بدون تضارب
    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    @AfterMethod
    public void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            driver.quit();
        }
        // تنظيف الـ ThreadLocal بعد إغلاق المتصفح لمنع تسريب الذاكرة
        driverThreadLocal.remove();
    }
}