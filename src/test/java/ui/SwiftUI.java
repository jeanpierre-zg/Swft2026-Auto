package ui;

import org.openqa.selenium.By;

/**
 * REPOSITORIO DE OBJETOS - SWIFT UAT
 * Define aquí los selectores para el flujo de autenticación.
 */
public class SwiftUI {
    // Pantalla de Selección de Método
    public static final By BOTON_DOS_FACTORES = By.xpath("//div[contains(@class,'auth_LL') and .//div[normalize-space()='User name and password']]//div[contains(@class,'auth_JL') and @tabindex='0']");

    // Pantalla de Login
    public static final By CAMPO_USUARIO = By.id("gwt-debug-platform_login-username");
    public static final By CAMPO_PASSWORD = By.id("gwt-debug-platform_login-password");
    public static final By BOTON_INGRESAR = By.id("gwt-debug-platform_login-logon");


    // Pantalla Home (Validación)
    public static final By TITULO_HOME = By.id("gwt-debug-desktop-mainmenu-Home");
}