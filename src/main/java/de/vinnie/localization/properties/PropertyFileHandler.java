package de.vinnie.localization.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertyFileHandler {

    private static volatile PropertyFileHandler propertyFileHandler;

    private PropertyFileHandler() {}

    public static PropertyFileHandler getPropertyFileHandler() {
        if (propertyFileHandler == null) {
            synchronized (PropertyFileHandler.class) {
                if (propertyFileHandler == null) {
                    propertyFileHandler = new PropertyFileHandler();
                }
            }
        }
        return propertyFileHandler;
    }

    public Properties loadProperties(String directory, String fileName) {
        java.util.Properties properties = new java.util.Properties();
        String fullPath = directory + "/" + fileName;

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fullPath)) {
            if (inputStream == null) {
                throw new IOException("PropertiesLoader file " + fullPath + " not found");
            }
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from " + fullPath, e);
        }
        return properties;
    }
}