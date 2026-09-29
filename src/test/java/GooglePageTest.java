import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GooglePageTest extends BaseTest {
    private void load(String html) {
        getDriver().get("data:text/html;charset=utf-8," +
                URLEncoder.encode(html, StandardCharsets.UTF_8).replace("+", "%20"));
    }

    @Test
    public void waitsForPriceTextAndIgnoresOtherQuotes() {
        load("<div class='YMlA3e'>$999.00</div><div class='YMlA3e fxKbKc' id='quote'></div>"
                + "<script>setTimeout(()=>document.getElementById('quote').textContent='$123.45',700)</script>");
        Assert.assertEquals(new GooglePage(getDriver(), Duration.ofSeconds(3)).getPriceText(), "$123.45");
    }

    @Test
    public void readsNewLayoutPrimaryQuoteInsteadOfChartValue() {
        load("<div class='vhBV3d'>Current <span jsname='Pdsbrc'>$999.00</span></div>"
                + "<div class='N6SYTe'><span jsname='Pdsbrc'><span>$123.45</span></span></div>");
        Assert.assertEquals(new GooglePage(getDriver(), Duration.ofSeconds(3)).getPriceText(), "$123.45");
    }

    @Test(expectedExceptions = TimeoutException.class)
    public void unrelatedQuoteDoesNotPass() {
        load("<div class='YMlA3e'>$999.00</div>");
        new GooglePage(getDriver(), Duration.ofSeconds(1)).getPriceText();
    }
}
