package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * UTILIDAD: ConfigReader
 * Proporciona acceso a los valores definidos en 'config.properties'.
 * Permite que el framework sea dinámico y no tenga datos quemados (Hardcoded).
 */
public class ConfigReader {
    private static Properties properties;

    // Bloque estático: se ejecuta una sola vez al cargar la clase en memoria
    static {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream("src/test/resources/config.properties"), StandardCharsets.UTF_8)) {
            properties = new Properties();
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException("ERROR: No se encontró el archivo src/test/resources/config.properties", e);
        }
    }

    /** Retorna un valor de texto */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /** Retorna un valor numérico (útil para tiempos de espera) */
    public static int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
}