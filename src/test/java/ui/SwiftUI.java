package ui;

import org.openqa.selenium.By;

/**
 * REPOSITORIO DE OBJETOS - SWIFT UAT
 * Define aquí los selectores para el flujo de autenticación.
 */
public class SwiftUI {
    // Pantalla de Selección de Método
    public static final By BOTON_DOS_FACTORES = By.id("ID_GENERICO_BOTON_2FA");

    // Pantalla de Login
    public static final By CAMPO_USUARIO = By.name("ID_GENERICO_USER");
    public static final By CAMPO_PASSWORD = By.name("ID_GENERICO_PASS");
    public static final By BOTON_INGRESAR = By.xpath("//button[text()='Ingresar']");

    // Pantalla Home (Validación)
    public static final By TITULO_HOME = By.id("ID_GENERICO_HOME_TITLE");
}