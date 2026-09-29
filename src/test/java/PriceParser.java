import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PriceParser {
    // Google Finance is explicitly requested in English (US number formatting).
    private static final Pattern PRICE = Pattern.compile(
            "^(?:US\\$|\\$)?\\s*((?:[0-9]+|[0-9]{1,3}(?:,[0-9]{3})+)(?:\\.[0-9]+)?)\\s*(?:USD)?$");

    public static BigDecimal parse(String text) {
        if (text == null) throw new IllegalArgumentException("Price is missing");
        Matcher matcher = PRICE.matcher(text.replace('\u00a0', ' ').trim());
        if (!matcher.matches()) throw new IllegalArgumentException("Invalid USD price: " + text);
        return new BigDecimal(matcher.group(1).replace(",", ""));
    }
}
