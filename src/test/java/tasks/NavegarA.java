package tasks;

import org.openqa.selenium.WebDriver;

/**
 * TAREA: NavegarA
 * Clase dedicada exclusivamente a la acción de abrir una dirección URL en el navegador.
 */
public class NavegarA {
    private final String url;

    public NavegarA(String url) {
        this.url = url;
    }

    public static NavegarA laPagina(String url) {
        return new NavegarA(url);
    }

    /**
     * Utiliza el método nativo get del driver para cargar la página web.
     */
    public void realizarComo(WebDriver driver) {
        driver.get(url);
    }
}