package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;
import java.time.Duration;
import java.util.Collections;

/**
 * UTILIDAD: DriverManager
 * Gestiona el ciclo de vida del WebDriver utilizando el patrón Singleton.
 * Se encarga de la configuración técnica de los navegadores (Edge/Chrome).
 */
public class DriverManager {
    private static WebDriver driver;

    /**
     * Obtiene la instancia del driver. Si no existe, la crea basándose 
     * en el archivo de configuración.
     */
    public static WebDriver getDriver() {
        if (driver == null) {
            // Leemos el navegador deseado desde el archivo config.properties
            String browser = ConfigReader.getProperty("browser").toLowerCase();
            
            if (browser.equals("edge")) {
                // Lógica de carga del driver local para Edge
                String driverPath = System.getProperty("user.dir") + File.separator + "drivers" + File.separator + "msedgedriver.exe";
                if (new File(driverPath).exists()) {
                    System.setProperty("webdriver.edge.driver", driverPath);
                }
                
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--disable-blink-features=AutomationControlled");
                options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                options.addArguments("--inprivate"); // Modo incógnito
                
                driver = new EdgeDriver(options);
            } else {
                // Configuración básica para Chrome
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-blink-features=AutomationControlled");
                driver = new ChromeDriver(options);
            }

            // Configuraciones globales del navegador
            driver.manage().window().maximize();
            // Espera implícita para dar tiempo a los elementos a aparecer
            int timeout = ConfigReader.getIntProperty("timeout.seconds");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
        }
        return driver;
    }

    /**
     * Cierra el navegador y destruye la instancia para liberar memoria.
     */
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}