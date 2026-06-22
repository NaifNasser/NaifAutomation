import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    // هذه الميثود تعمل تلقائياً أول ما يفشل أي تيست
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("❌ Test Failed: " + result.getName() + " -> Taking Screenshot...");

        // جلب الـ driver المشغل حالياً من كلاس التيست
        Object currentClass = result.getInstance();
        WebDriver driver = ((BaseTest) currentClass).getDriver();

        if (driver != null) {
            saveScreenshot(driver);
        }
    }

    // الـ Annotation هذا يخبر Allure أن هذه الصورة تابعة للتقرير
    @Attachment(value = "Page Screenshot on Failure", type = "image/png")
    public byte[] saveScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}