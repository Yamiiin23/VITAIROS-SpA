# VITAIROS-M · App móvil (DSY1105 · Evaluación Parcial 2)

Prototipo Android en **Kotlin + Jetpack Compose + Material 3** de la experiencia móvil de orientación y acompañamiento de VITAIROS-M.

**Perfil de prueba:** `demo@vitairos.test` / `Demo1234` (botón "Usar perfil ficticio de prueba").

## Cómo abrirlo
1. Android Studio Koala o superior (JDK 17). `File > Open` sobre esta carpeta y esperar el Gradle Sync.
2. Ejecutar en un emulador o teléfono Android 8.0+ (minSdk 26).

## Arquitectura (modularidad)
```
cl.vitairos.vitairosm
├── data/        local (Room: entidades, DAO, BD) · repository · session · AppContainer (inyección manual)
├── model/       catálogos
├── ui/          theme (marca) · components · screens · navigation
├── util/        Notifier · ConnectivityObserver · Formatters · validation (Validators: reglas centralizadas)
├── viewmodel/   Auth / Mood / Appointment + factory (estado con StateFlow)
```

## Cobertura de la rúbrica
| Indicador | Dónde se cumple |
|---|---|
| 2.1.1 Interfaz coherente y navegación | `ui/navigation/AppNavigation.kt` (bottom bar + rutas), `ui/theme`, `ui/screens` |
| 2.1.2 Formularios validados | Login, Registro emocional y Cita: `ValidatedTextField`, `ChoiceChips`, `MoodSelector` (ícono de estado + mensaje por campo) |
| 2.2.1 Validación desacoplada y estado | `util/validation/Validators.kt` + `*UiState` en ViewModels |
| 2.2.2 Animaciones funcionales | Entrada escalonada (`Appear`), botón con pulsación, chips/selector con `animateColorAsState`, errores con `AnimatedVisibility`, progreso semanal, `animateItem`, transiciones de navegación |
| 2.3.1 Modularidad y persistencia local | Capas separadas + **Room** (registros y citas) + SharedPreferences (sesión) |
| 2.3.2 Colaboración y control de versiones | Colaboración y control de versiones mediante repositorio en GitHub |
| 2.4.1 Recursos del dispositivo | **Notificaciones** locales (permiso POST_NOTIFICATIONS), **estado de red** (banner sin conexión), **marcador telefónico** (Ayuda), **háptica** al guardar |

## Pendientes antes de entregar
Litas
