package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.es.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import tasks.LoguearseEnSwift;
import tasks.NavegarA;
import ui.SwiftUI;
import utils.ConfigReader;
import utils.DriverManager;
import utils.EvidenceHelper;

public class SwiftSteps {
    private WebDriver driver;
    private EvidenceHelper evidence;
    private Scenario scenario;

    private static final String FOLDER_EJECUCION = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

    @Before("@Swift")
    public void setUp(Scenario scenario) {
        this.scenario = scenario;
        try {
            this.driver = DriverManager.getDriver();
            this.evidence = new EvidenceHelper(scenario.getName(), ConfigReader.getProperty("qa.analyst"));
        } catch (Exception e) {
            System.err.println("CRÍTICO: Falló el inicio del driver en Swift: " + e.getMessage());
        }
    }

    /**
     * Método de registro con captura de pantalla forzada.
     * Si falla la captura, al menos deja un log en consola.
     */
    private void registrar(String paso, String desc) {
        if (driver == null) return;
        try {
            byte[] ss = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(ss, "image/png", paso);
            evidence.agregarPaso(paso, desc, ss);
        } catch (Exception e) {
            System.err.println("No se pudo tomar la captura en el paso: " + paso);
        }
    }

    @Dado("que el analista ingresa al portal de Swift UAT")
    public void ingresarPortal() {
        try {
            NavegarA.laPagina(ConfigReader.getProperty("url.swift.uat")).realizarComo(driver);
            registrar("Acceso Swift", "Navegación a la URL de Swift UAT");
        } catch (Exception e) {
            registrar("ERROR ACCESO", "No se pudo cargar la URL: " + e.getMessage());
            throw e;
        }
    }

    @Cuando("selecciona el método de autenticación por dos factores")
    public void seleccionar2FA() {
        try {
            driver.findElement(SwiftUI.BOTON_DOS_FACTORES).click();
            registrar("Selección 2FA", "Selección del método de autenticación doble factor");
        } catch (Exception e) {
            registrar("ERROR 2FA", "No se encontró el botón de 2FA: " + e.getMessage());
            throw e;
        }
    }

    @Y("se autentica con el rol de {string}")
    public void autenticarseConRol(String rol) {
        try {
            String usuario = rol.equalsIgnoreCase("procesador") 
                ? ConfigReader.getProperty("swift.procesador.user") 
                : ConfigReader.getProperty("swift.liberador.user");
            
            String password = rol.equalsIgnoreCase("procesador") 
                ? ConfigReader.getProperty("swift.procesador.pass") 
                : ConfigReader.getProperty("swift.liberador.pass");

            LoguearseEnSwift.conCredenciales(usuario, password).realizarComo(driver);
            registrar("Login Swift", "Autenticación como usuario con rol: " + rol);
        } catch (Exception e) {
            registrar("ERROR LOGIN", "Falló el ingreso de credenciales para: " + rol);
            throw e;
        }
    }

    @Entonces("debería visualizar el home principal de Swift")
    public void validarHome() {
        try {
            boolean esVisible = driver.findElement(SwiftUI.TITULO_HOME).isDisplayed();
            registrar("Validación Home", "Carga exitosa del dashboard principal");
            Assert.assertTrue(esVisible, "ERROR: No se visualiza el home de Swift.");
        } catch (Exception e) {
            registrar("ERROR VALIDACIÓN", "El Home no cargó o el elemento no fue encontrado");
            throw e;
        }
    }

    @After("@Swift")
    public void tearDown() {
        if (driver != null) {
            if (scenario.isFailed()) {
                try {
                    // Tomamos una captura final del estado exacto del error
                    byte[] ss = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    evidence.agregarPasoFallido("PANTALLA DEL ERROR", "Estado del navegador al momento de la falla", ss);
                    evidence.marcarComoFallido();
                } catch (Exception e) {
                    System.err.println("No se pudo tomar la captura final de error.");
                }
            }

            // Guardado del reporte antes de cerrar el driver
            String status = scenario.isFailed() ? "FALLIDO" : "OK";
            String fileName = scenario.getName().replace(" ", "_") + " - " + status + ".docx";
            evidence.guardarDocumento("target/evidencias/Ejecucion_" + FOLDER_EJECUCION + "/" + fileName);
            
            DriverManager.quitDriver();
        }
    }
}