package utils;

import java.io.FileInputStream;
import java.io.IOException;
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
        try {
            // Ruta del archivo de configuración
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("ERROR: No se encontró el archivo src/test/resources/config.properties");
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