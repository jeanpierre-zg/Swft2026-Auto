package tasks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.GoogleSearchUI;
import java.time.Duration;

public class IngresarAlPrimerResultado {

    public static IngresarAlPrimerResultado delBuscador() {
        return new IngresarAlPrimerResultado();
    }

    public void realizarComo(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement primerLink = wait.until(ExpectedConditions.elementToBeClickable(GoogleSearchUI.PRIMER_RESULTADO));
        primerLink.click();
    }
}