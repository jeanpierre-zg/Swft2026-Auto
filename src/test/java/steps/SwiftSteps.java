package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.es.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.testng.Assert;
import org.testng.Reporter;
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
            // Esperar a que el documento esté completamente cargado antes de capturar la primera evidencia
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
            // Pequeño retardo para asegurar renderizado visual antes de la captura
            try { Thread.sleep(500); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            registrar("Acceso Swift", "Navegación a la URL de Swift UAT - página cargada");
        } catch (Exception e) {
            registrar("ERROR ACCESO", "No se pudo cargar la URL: " + e.getMessage());
            throw e;
        }
    }

    @Cuando("selecciona el método de autenticación por dos factores")
    public void seleccionar2FA() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement boton = wait.until(ExpectedConditions.visibilityOfElementLocated(SwiftUI.BOTON_DOS_FACTORES));
            // Asegurar que esté en viewport
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", boton);
            // Pequeña espera para renderizado estable
            try { Thread.sleep(500); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }

            // Captura la pantalla antes de hacer click (evidencia)
            registrar("Selección 2FA - Antes", "Se visualiza el botón de 2FA antes de hacer click");

            // Click con espera de clicabilidad
            wait.until(ExpectedConditions.elementToBeClickable(SwiftUI.BOTON_DOS_FACTORES)).click();

            // Captura posterior al click por si se requiere evidencia del cambio
            registrar("Selección 2FA - Después", "Se hizo click en el botón de 2FA");
        } catch (Exception e) {
            registrar("ERROR 2FA", "No se encontró o no fue posible interactuar con el botón de 2FA: " + e.getMessage());
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

            // Ingresar credenciales con esperas y tomar evidencia clara
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement usuarioElem = wait.until(ExpectedConditions.visibilityOfElementLocated(SwiftUI.CAMPO_USUARIO));
            usuarioElem.clear();
            usuarioElem.sendKeys(usuario);

            WebElement passElem = wait.until(ExpectedConditions.visibilityOfElementLocated(SwiftUI.CAMPO_PASSWORD));
            passElem.clear();
            passElem.sendKeys(password);

            // Log genérico sin exponer credenciales
            Reporter.log("[LOGIN] Intentando login", true);

            // Captura después de completar credenciales (única evidencia de credenciales)
            registrar("Credenciales ingresadas", "Usuario y contraseña ingresados para rol: " + rol);

            // Asegurar que el botón de ingresar sea clickable y hacer click
            WebElement btnLogin = wait.until(ExpectedConditions.elementToBeClickable(SwiftUI.BOTON_INGRESAR));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnLogin);
            btnLogin.click();

            // Captura posterior al login
            registrar("Después Login", "Se intentó el ingreso con las credenciales proporcionadas");
        } catch (Exception e) {
            registrar("ERROR LOGIN", "Falló el ingreso de credenciales para: " + rol + " - " + e.getMessage());
            throw e;
        }
    }

    @Entonces("debería visualizar el home principal de Swift")
    public void validarHome() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            WebElement titulo = wait.until(ExpectedConditions.visibilityOfElementLocated(SwiftUI.TITULO_HOME));
            registrar("Validación Home", "Carga exitosa del dashboard principal");
            Assert.assertTrue(titulo.isDisplayed(), "ERROR: No se visualiza el home de Swift.");
        } catch (Exception e) {
            registrar("ERROR VALIDACIÓN", "El Home no cargó o el elemento no fue encontrado: " + e.getMessage());
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