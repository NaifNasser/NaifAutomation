import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getResourceAsStream("/config.properties")) {
            if (input == null) throw new IllegalStateException("Missing config.properties on classpath");
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read config.properties", e);
        }
    }

    public static String getProperty(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }
}
