# language: es
@Swift @Regresion
Característica: Login en Swift UAT

  Como analista QA
  Quiero iniciar sesión en Swift con los roles definidos
  Para validar el acceso y generar evidencias

  @SwiftProcesador
  Escenario: Login con usuario Procesador
    Dado que el analista ingresa al portal de Swift UAT
    Cuando selecciona el método de autenticación por dos factores
    Y se autentica con el rol de "procesador"
    Entonces debería visualizar el home principal de Swift

   @SwiftLiberador
  Escenario: Login con usuario Liberador
    Dado que el analista ingresa al portal de Swift UAT
    Cuando selecciona el método de autenticación por dos factores
    Y se autentica con el rol de "liberador"
    Entonces debería visualizar el home principal de Swift
