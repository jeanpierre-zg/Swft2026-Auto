package tasks;

import org.openqa.selenium.WebDriver;
import ui.SwiftUI;

/**
 * TAREA: LoguearseEnSwift
 * Encapsula la acción de ingresar credenciales en el portal de Swift.
 */
public class LoguearseEnSwift {
    private final String usuario;
    private final String password;

    public LoguearseEnSwift(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }

    public static LoguearseEnSwift conCredenciales(String user, String pass) {
        return new LoguearseEnSwift(user, pass);
    }

    public void realizarComo(WebDriver driver) {
        driver.findElement(SwiftUI.CAMPO_USUARIO).sendKeys(usuario);
        driver.findElement(SwiftUI.CAMPO_PASSWORD).sendKeys(password);
        driver.findElement(SwiftUI.BOTON_INGRESAR).click();
    }
}