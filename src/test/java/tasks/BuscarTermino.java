package tasks;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.GoogleSearchUI;
import java.time.Duration;

/**
 * TAREA: BuscarTermino
 * Representa la acción de escribir una palabra en el buscador y presionar ENTER.
 * Sigue el patrón Screenplay al encapsular la lógica de interacción en una clase reutilizable.
 */
public class BuscarTermino {
    private final String termino;

    public BuscarTermino(String termino) {
        this.termino = termino;
    }

    // Método factoría para permitir una escritura fluida: BuscarTermino.elTermino("...")
    public static BuscarTermino elTermino(String termino) {
        return new BuscarTermino(termino);
    }

    /**
     * Realiza la acción de búsqueda utilizando el driver proporcionado.
     * Incluye una espera explícita para asegurar que el elemento esté disponible.
     */
    public void realizarComo(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Localizamos el elemento usando la constante definida en la capa UI
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(GoogleSearchUI.CAJA_BUSQUEDA));

        searchBox.clear();           // Limpia cualquier texto previo
        searchBox.sendKeys(termino); // Escribe el término de búsqueda
        searchBox.sendKeys(Keys.ENTER); // Presiona la tecla Enter para ejecutar
    }
}