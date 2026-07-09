# language: es
@Swift @Regresion
Característica: Autenticación en el portal Swift UAT

  @SwiftProcesador
  Escenario: Acceso exitoso como usuario Procesador mediante doble factor
    Dado que el analista ingresa al portal de Swift UAT
    Cuando selecciona el método de autenticación por dos factores
    Y se autentica con el rol de "procesador"
    Entonces debería visualizar el home principal de Swift

  @SwiftLiberador
  Escenario: Acceso exitoso como usuario Liberador mediante doble factor
    Dado que el analista ingresa al portal de Swift UAT
    Cuando selecciona el método de autenticación por dos factores
    Y se autentica con el rol de "liberador"
    Entonces debería visualizar el home principal de Swift
