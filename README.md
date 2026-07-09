# 🚀 Framework de Automatización QA: Selenium, Cucumber, Screenplay & Reportes Word

¡Bienvenido al Framework de Automatización QA! Este proyecto está diseñado para realizar pruebas automatizadas de extremo a extremo (E2E) utilizando un stack de tecnologías moderno y generar reportes de evidencia detallados en formato Word (.docx).

---

## ✨ Características Principales

-   **Automatización Web Robusta**: Utiliza Selenium WebDriver para interactuar con navegadores web (Edge y Chrome).
-   **BDD con Cucumber**: Casos de prueba escritos en lenguaje natural (Gherkin) para facilitar la colaboración entre equipos técnicos y no técnicos.
-   **Patrón Screenplay Simplificado**: Una arquitectura limpia y mantenible que separa "qué" se hace (UI), "cómo" se hace (Tasks) y "quién" lo hace (Steps).
-   **Generación Automática de Evidencias**: Crea reportes ejecutivos en formato Word (.docx) con capturas de pantalla por cada paso, incluyendo el estado de éxito o falla.
-   **Configuración Dinámica**: Permite cambiar el navegador, URLs y credenciales sin modificar el código fuente, gracias a un archivo `config.properties`.
-   **Gestión de Roles**: Soporte para autenticación de diferentes tipos de usuarios (ej. Procesador, Liberador) en flujos complejos.

---

## 🛠️ Tecnologías Utilizadas

-   **Java 17+**
-   **Maven**: Gestión de dependencias y construcción del proyecto.
-   **Selenium WebDriver 4.x**: Interacción con el navegador.
-   **Cucumber JVM**: Implementación de BDD.
-   **TestNG**: Ejecutor de pruebas.
-   **Apache POI**: Generación de documentos Word (.docx).

---

## ⚙️ Requisitos Previos

Para clonar, construir y ejecutar este proyecto, necesitarás tener instalado lo siguiente en tu sistema:

1.  **Java Development Kit (JDK) 17 o superior**:
    -   Descarga e instala desde [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) o [Adoptium OpenJDK](https://adoptium.net/).
    -   Asegúrate de que la variable de entorno `JAVA_HOME` esté configurada y que `java -version` en tu terminal muestre la versión correcta.

2.  **Apache Maven 3.x o superior**:
    -   Descarga e instala desde [Apache Maven](https://maven.apache.org/download.cgi).
    -   Asegúrate de que `mvn -version` funcione en tu terminal.

3.  **IntelliJ IDEA Community/Ultimate (Recomendado)**:
    -   Descarga e instala desde [JetBrains IntelliJ IDEA](https://www.jetbrains.com/idea/download/).
    -   Aunque puedes usar otros IDEs, este proyecto está optimizado para IntelliJ.

4.  **Navegador Web**:
    -   **Microsoft Edge** o **Google Chrome** instalados en tu sistema.

5.  **Drivers de Navegador**:
    -   **IMPORTANTE**: Este framework está configurado para buscar los drivers (`msedgedriver.exe` o `chromedriver.exe`) en la carpeta `drivers/` dentro de la raíz del proyecto.
    -   **Descarga el driver compatible con la versión de tu navegador**:
        -   **Edge**: [Microsoft Edge Driver](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/)
        -   **Chrome**: [Chrome for Testing](https://googlechromelabs.github.io/chrome-for-testing/) (busca la versión compatible con tu Chrome).
    -   Crea una carpeta llamada `drivers` en la raíz de tu proyecto (donde está `pom.xml`) y coloca el ejecutable del driver (`msedgedriver.exe` o `chromedriver.exe`) dentro.

---

## 🚀 Configuración y Ejecución

Sigue estos pasos para poner en marcha el proyecto:

### 1. Clonar el Repositorio

Abre tu terminal o Git Bash y ejecuta:

```bash
git clone https://github.com/jeanpierre-zg/Swft2026-Auto.git
cd selenium-cucumber-testing-word
```

### 2. Abrir el Proyecto en IntelliJ IDEA

1.  Abre IntelliJ IDEA.
2.  Selecciona `File` > `Open...`.
3.  Navega a la carpeta donde clonaste el proyecto (`selenium-cucumber-testing-word`) y haz clic en `Open`.
4.  IntelliJ debería detectar automáticamente que es un proyecto Maven e importar las dependencias. Si no lo hace, busca la pestaña `Maven` en el lateral derecho y haz clic en `Reload All Maven Projects`.

### 3. Configurar `config.properties`

Este archivo te permite personalizar la ejecución sin tocar el código.

1.  Abre `src/test/resources/config.properties`.
2.  **`browser`**: Cambia a `edge` o `chrome` según el navegador que quieras usar.
3.  **`url.swift.uat`**: Reemplaza `https://coloca-aqui-el-link-de-swift-uat.com` con la URL real de tu entorno Swift UAT.
4.  **`swift.procesador.user` / `swift.procesador.pass`**: Ingresa las credenciales del usuario procesador.
5.  **`swift.liberador.user` / `swift.liberador.pass`**: Ingresa las credenciales del usuario liberador.

### 4. Ejecutar las Pruebas

Las pruebas se ejecutan a través del `RunnerTest` de TestNG.

1.  Abre el archivo `src/test/java/runner/RunnerTest.java`.
2.  **Selección de Escenarios**: Modifica la línea `tags = "@Regresion"` (o la que esté configurada) para elegir qué escenarios ejecutar:
    -   `tags = "@PruebaGoogle or @PruebaNavegacion"`: Ejecuta solo los escenarios de Google.
    -   `tags = "@Swift"`: Ejecuta solo los escenarios de Swift.
    -   `tags = "@Regresion"`: Ejecuta todos los escenarios marcados con `@Regresion` (Google y Swift).
    -   `tags = "@SwiftProcesador"`: Ejecuta solo el escenario de Swift para el usuario procesador.
    -   `tags = "@SwiftLiberador"`: Ejecuta solo el escenario de Swift para el usuario liberador.
3.  **Ejecutar**: Haz clic derecho en cualquier parte del código de `RunnerTest.java` y selecciona `Run 'RunnerTest'`.

---

## 📂 Estructura del Proyecto

El proyecto sigue una arquitectura modular para facilitar el mantenimiento y la escalabilidad:

-   `src/test/java/steps/`: Contiene las clases que conectan los pasos de Gherkin con el código Java (Step Definitions).
-   `src/test/java/tasks/`: Encapsula las acciones de negocio que el "actor" realiza (ej. `BuscarTermino`, `LoguearseEnSwift`).
-   `src/test/java/ui/`: Almacena los localizadores de los elementos de la interfaz de usuario (ej. `GoogleSearchUI`, `SwiftUI`).
-   `src/test/java/utils/`: Clases de utilidad como `DriverManager` (gestión del navegador), `ConfigReader` (lectura de propiedades) y `EvidenceHelper` (generación de reportes Word).
-   `src/test/java/runner/`: Contiene el `RunnerTest` para la ejecución de las pruebas.
-   `src/test/resources/features/`: Archivos `.feature` con los escenarios de prueba en Gherkin.
-   `src/test/resources/config.properties`: Archivo de configuración del entorno.
-   `drivers/`: Carpeta para los ejecutables de los drivers de navegador.

---

## 📊 Reportes y Evidencias

Al finalizar la ejecución de las pruebas, se generarán los siguientes reportes:

-   **Reporte HTML de Cucumber**: `target/cucumber-reports/cucumber.html`
-   **Reporte JSON de Cucumber**: `target/cucumber-reports/cucumber.json`
-   **Reportes de Evidencia en Word**: `target/evidencias/Ejecucion_[FECHA_HORA]/`
    -   Cada escenario genera un archivo `.docx` con el nombre del escenario y su estado (OK/FALLIDO), incluyendo capturas de pantalla detalladas de cada paso.

---

## 👤 Autor

**Jean Pierre Laurente Zambrano**

<img src="https://github.com/jeanpierre-zg.png" width="100" height="100" alt="Foto de perfil de Jean Pierre Laurente Zambrano">

---

## 📄 Licencia

Este proyecto se distribuye bajo la licencia MIT. Consulta el archivo `LICENSE` para más detalles.

---
