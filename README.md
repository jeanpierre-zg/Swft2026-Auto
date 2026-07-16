# ⚠️ ADVERTENCIA DE CONFIDENCIALIDAD ⚠️

**Este proyecto contiene información sensible y propietaria de Interbank, específicamente relacionada con el proyecto SWIFT 2026.**

**SU USO ESTÁ RESTRINGIDO EXCLUSIVAMENTE A PERSONAL AUTORIZADO DE INTERBANK (QA's y equipos de desarrollo involucrados).**

**La divulgación, reproducción o distribución no autorizada de este código o de la información contenida en él, incluyendo detalles de la implementación de SWIFT 2026, credenciales, URLs de entornos o cualquier otro dato sensible, está estrictamente prohibida y puede acarrear GRAVES CONSECUENCIAS LEGALES conforme a la legislación vigente y las políticas internas de Interbank.**

**Al acceder y utilizar este repositorio, usted reconoce y acepta su obligación de mantener la confidencialidad de la información y de cumplir con todas las leyes y políticas aplicables.**

---

# 🚀 Proyecto: Automatización SWIFT 2026

Repositorio orientado exclusivamente a la automatización del portal SWIFT UAT (SWIFT 2026). Contiene el framework de automatización, casos BDD y utilidades para generar evidencias y reportes.

---

## ✨ Objetivo

Automatizar los flujos principales de acceso e interacción en el portal SWIFT UAT para pruebas funcionales y de regresión, generando evidencias en Word por cada paso ejecutado.

---

## 🛠️ Tecnologías Principales

-   Java 17+
-   Maven
-   Selenium WebDriver 4.x
-   Cucumber JVM
-   TestNG
-   Apache POI (generación de .docx)

---

## ⚙️ Requisitos Previos

1.  Java JDK 17+
2.  Maven 3.x+
3.  IntelliJ IDEA (recomendado)
4.  Microsoft Edge (recomendado) o Chrome
5.  Drivers: coloca `msedgedriver.exe` o `chromedriver.exe` en la carpeta `drivers/` de la raíz del proyecto.

---

## 🚀 Configuración y Ejecución (SWIFT)

1.  Clona el repositorio y abre el proyecto en tu IDE.
2.  Edita `src/test/resources/config.properties` con los valores de `url.swift.uat` y credenciales (swift.procesador.*, swift.liberador.*).
3.  En `src/test/java/runner/RunnerTest.java` ajusta el tag de ejecución:
    - `tags = "@Swift"` — ejecutar solo escenarios Swift.
    - `tags = "@Regresion"` — ejecutar la suite de regresión (actualmente Swift).
4.  Ejecuta `RunnerTest` desde IntelliJ o con `mvn -Dtestng.groups=@Swift test`.

---

## 📂 Estructura del Proyecto

- `src/test/java/steps/` — Step Definitions (ej.: SwiftSteps)
- `src/test/java/tasks/` — Tasks reutilizables (ej.: LoguearseEnSwift, NavegarA)
- `src/test/java/ui/` — Localizadores (ej.: SwiftUI)
- `src/test/java/utils/` — Utilidades (DriverManager, ConfigReader, EvidenceHelper)
- `src/test/java/runner/` — RunnerTest
- `src/test/resources/features/` — Scenarios Gherkin para Swift
- `src/test/resources/config.properties` — Configuración por ambiente
- `drivers/` — Ejecutables de webdriver

---

## 📊 Reportes y Evidencias

- Reporte HTML: `target/cucumber-reports/cucumber.html`
- Reporte JSON: `target/cucumber-reports/cucumber.json`
- Evidencias en Word: `target/evidencias/Ejecucion_[FECHA_HORA]/` (cada escenario genera un `.docx` con capturas por paso)

---

## 📝 Git

Mantener el código fuente y configuración en el repositorio. Los artefactos de ejecución (reportes, evidencias) están en `.gitignore`.

---

## 👤 Autor

Jean Pierre Laurente Zambrano

---
