# language: es
Característica: Búsqueda de información en Google con Screenplay

  Como un usuario en internet
  Quiero realizar búsquedas en Google usando el patrón Screenplay
  Para encontrar contenido relevante de automatización y visitar sitios web de interés

  @PruebaGoogle @Regresion
  Escenario: Búsqueda exitosa del término de automatización
    Dado que el actor "Jean" está en la página de búsqueda de Google
    Cuando busca el término "Selenium Cucumber Java automation"
    Entonces debería ver resultados relacionados con la búsqueda

  @PruebaNavegacion @Regresion
  Escenario: Buscar otro término e ingresar al primer resultado
    Dado que el actor "Jean" está en la página de búsqueda de Google
    Cuando busca el término "Eiffel Tower Wikipedia"
    Y hace clic en el primer resultado de la búsqueda
    Entonces debería ver el título de la página o información del primer resultado