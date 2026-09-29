import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    // هذه الميثود تعمل تلقائياً أول ما يفشل أي تيست
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("❌ Test Failed: " + result.getName() + " -> Taking Screenshot...");

        // جلب الـ driver المشغل حالياً من كلاس التيست
        Object currentClass = result.getInstance();
        if (!(currentClass instanceof BaseTest)) return;
        WebDriver driver = ((BaseTest) currentClass).getDriver();

        if (driver != null) {
            try {
                saveScreenshot(driver);
            } catch (WebDriverException e) {
                System.err.println("Screenshot failed: " + e.getMessage());
            }
        }
    }

    // إرفاق مباشر يعمل في Maven وIntelliJ بدون AspectJ
    public byte[] saveScreenshot(WebDriver driver) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment("Page Screenshot on Failure", "image/png",
                new ByteArrayInputStream(screenshot), ".png");
        return screenshot;
    }
}