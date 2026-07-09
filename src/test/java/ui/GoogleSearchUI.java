package ui;

import org.openqa.selenium.By;

/**
 * CAPA UI: GoogleSearchUI
 * Esta clase funciona como un repositorio de objetos (Object Repository).
 * Centraliza los localizadores de la página de inicio de Google.
 * Si el diseño de la web cambia, solo se debe actualizar este archivo.
 */
public class GoogleSearchUI {
    // Selector para la caja de texto de búsqueda (usando el nombre del atributo 'q')
    public static final By CAJA_BUSQUEDA = By.name("q");
    
    // Selector para el contenedor principal de los resultados de búsqueda
    public static final By CONTENEDOR_RESULTADOS = By.id("search");
    
    // Selector para el primer enlace de los resultados de búsqueda (usando XPath)
    public static final By PRIMER_RESULTADO = By.xpath("(//h3)[1]");
}