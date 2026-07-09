package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.es.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import tasks.*;
import ui.*;
import utils.EvidenceHelper;
import utils.DriverManager;
import utils.ConfigReader;

public class GoogleSearchSteps {
    private WebDriver driver;
    private EvidenceHelper evidence;
    private Scenario scenario;

    private static final String FOLDER_EJECUCION = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

    @Before("@PruebaGoogle or @PruebaNavegacion")
    public void setUp(Scenario scenario) {
        this.scenario = scenario;
        this.driver = DriverManager.getDriver();
        this.evidence = new EvidenceHelper(scenario.getName(), ConfigReader.getProperty("qa.analyst"));
    }

    private void registrar(String paso, String desc) {
        if (driver == null) return;
        try {
            byte[] ss = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(ss, "image/png", paso);
            evidence.agregarPaso(paso, desc, ss);
        } catch (Exception e) {
            System.err.println("Error capturando evidencia: " + e.getMessage());
        }
    }

    @Dado("que el actor {string} está en la página de búsqueda de Google")
    public void navegar(String actor) {
        try {
            NavegarA.laPagina(ConfigReader.getProperty("url.google")).realizarComo(driver);
            registrar("Navegación", actor + " inició búsqueda en Google");
        } catch (Exception e) {
            registrar("ERROR NAVEGACIÓN", "No se pudo cargar la URL de Google");
            throw e;
        }
    }

    @Cuando("busca el término {string}")
    public void buscar(String termino) {
        try {
            BuscarTermino.elTermino(termino).realizarComo(driver);
            registrar("Búsqueda", "Término buscado: " + termino);
        } catch (Exception e) {
            registrar("ERROR BÚSQUEDA", "Falló al intentar buscar el término");
            throw e;
        }
    }

    @Entonces("debería ver resultados relacionados con la búsqueda")
    public void validarResultados() {
        try {
            Assert.assertTrue(driver.findElement(GoogleSearchUI.CONTENEDOR_RESULTADOS).isDisplayed());
            registrar("Resultados", "Visualización de resultados exitosa");
        } catch (Exception e) {
            registrar("ERROR RESULTADOS", "No se visualizaron resultados de búsqueda");
            throw e;
        }
    }

    @Cuando("hace clic en el primer resultado de la búsqueda")
    public void clicPrimerResultado() {
        try {
            IngresarAlPrimerResultado.delBuscador().realizarComo(driver);
            registrar("Clic Resultado", "Ingreso a la página del primer resultado");
        } catch (Exception e) {
            registrar("ERROR CLIC", "No se pudo interactuar con el primer resultado");
            throw e;
        }
    }

    @Entonces("debería ver el título de la página o información del primer resultado")
    public void validarDestino() {
        try {
            Assert.assertTrue(driver.findElement(WikipediaUI.TITULO_ARTICULO).isDisplayed());
            registrar("Validación Final", "Título visible en la página de destino");
        } catch (Exception e) {
            registrar("ERROR VALIDACIÓN", "No se cargó el título en la página final");
            throw e;
        }
    }

    @After("@PruebaGoogle or @PruebaNavegacion")
    public void tearDown() {
        if (driver != null) {
            if (scenario.isFailed()) {
                try {
                    byte[] ss = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    evidence.agregarPasoFallido("FALLO DE ESCENARIO", "La prueba de Google falló", ss);
                    evidence.marcarComoFallido();
                } catch (Exception e) {}
            }
            
            String status = scenario.isFailed() ? "FALLIDO" : "OK";
            String fileName = scenario.getName().replace(" ", "_") + " - " + status + ".docx";
            evidence.guardarDocumento("target/evidencias/Ejecucion_" + FOLDER_EJECUCION + "/" + fileName);
            
            DriverManager.quitDriver();
        }
    }
}