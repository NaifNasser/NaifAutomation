import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Locale;

public class GooglePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    // Match the primary quote, not prices in recommendation cards.
    private final By price = By.cssSelector(".N6SYTe > span[jsname='Pdsbrc'], .YMlA3e.fxKbKc");
    private String symbol;
    private boolean explicitExchange;

    public GooglePage(WebDriver driver) {
        this(driver, Duration.ofSeconds(20));
    }

    GooglePage(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
        this.wait.ignoring(StaleElementReferenceException.class);
    }

    public void openStock(String stockSymbol) {
        symbol = stockSymbol.trim().toUpperCase(Locale.ROOT);
        if (!symbol.matches("[A-Z0-9.-]+(:[A-Z0-9]+)?")) {
            throw new IllegalArgumentException("Invalid stock symbol: " + stockSymbol);
        }
        explicitExchange = symbol.contains(":");
        driver.get("https://www.google.com/finance/quote/" + symbol
                + (explicitExchange ? "" : ":NASDAQ") + "?hl=en");
    }

    public String getPriceText() {
        try {
            return waitForPrice();
        } catch (QuoteNotFoundException first) {
            if (explicitExchange || symbol == null) throw first;
            driver.get("https://www.google.com/finance/quote/" + symbol + ":NYSE?hl=en");
            try {
                return waitForPrice();
            } catch (TimeoutException | QuoteNotFoundException second) {
                second.addSuppressed(first);
                throw second;
            }
        }
    }

    private static class QuoteNotFoundException extends IllegalStateException {
        QuoteNotFoundException(String message) {
            super(message);
        }
    }

    private String waitForPrice() {
        return wait.withMessage(() -> "No non-empty primary quote at " + driver.getCurrentUrl())
                .until(d -> {
                    String url = d.getCurrentUrl();
                    if (url.contains("/sorry/") || url.contains("consent.google.")) {
                        throw new IllegalStateException("Google requires consent or verification: " + url);
                    }
                    if (d.findElements(By.xpath("//*[normalize-space(text())='Page Not Found']"))
                            .stream().anyMatch(WebElement::isDisplayed)) {
                        throw new QuoteNotFoundException("Quote not found at " + url);
                    }
                    for (WebElement element : d.findElements(price)) {
                        if (element.isDisplayed() && !element.getText().isBlank()) {
                            return element.getText().trim();
                        }
                    }
                    return null;
                });
    }
}
