# VITAIROS-M · App móvil (DSY1105 · Evaluación Parcial 2)

Prototipo Android en **Kotlin + Jetpack Compose + Material 3** de la experiencia móvil de orientación y acompañamiento de VITAIROS-M.
Usa **solo datos ficticios**. No entrega diagnósticos ni reemplaza la atención profesional.

**Perfil de prueba:** `demo@vitairos.test` / `Demo1234` (botón "Usar perfil ficticio de prueba").

## Cómo abrirlo
1. Android Studio Koala o superior (JDK 17). `File > Open` sobre esta carpeta y esperar el Gradle Sync.
2. Ejecutar en un emulador o teléfono Android 8.0+ (minSdk 26).

## Arquitectura (modularidad)
```
cl.vitairos.vitairosm
├── data/        local (Room: entidades, DAO, BD) · repository · session
├── domain/      model (catálogos) · validation (Validators: reglas centralizadas)
├── viewmodel/   Auth / Mood / Appointment + factory (estado con StateFlow)
├── ui/          theme (marca) · components · screens · navigation
├── util/        Notifier · ConnectivityObserver · Formatters
└── di/          AppContainer (inyección manual)
```

## Cobertura de la rúbrica
| Indicador | Dónde se cumple |
|---|---|
| 2.1.1 Interfaz coherente y navegación | `ui/navigation/AppNavigation.kt` (bottom bar + rutas), `ui/theme`, `ui/screens` |
| 2.1.2 Formularios validados | Login, Registro emocional y Cita: `ValidatedTextField`, `ChoiceChips`, `MoodSelector` (ícono de estado + mensaje por campo) |
| 2.2.1 Validación desacoplada y estado | `domain/validation/Validators.kt` + `*UiState` en ViewModels; test en `ValidatorsTest.kt` |
| 2.2.2 Animaciones funcionales | Entrada escalonada (`Appear`), botón con pulsación, chips/selector con `animateColorAsState`, errores con `AnimatedVisibility`, progreso semanal, `animateItem`, transiciones de navegación |
| 2.3.1 Modularidad y persistencia local | Capas separadas + **Room** (registros y citas) + SharedPreferences (sesión) |
| 2.3.2 Colaboración y control de versiones | Ver sección Git (hay que hacerlo ustedes dos) |
| 2.4.1 Recursos del dispositivo | **Notificaciones** locales (permiso POST_NOTIFICATIONS), **estado de red** (banner sin conexión), **marcador telefónico** (Ayuda), **háptica** al guardar |

## Git (indicador 2.3.2, 20 %)
Esto no se puede automatizar: la rúbrica pide participación real de ambos.
1. Repositorio en GitHub/GitLab, ambos como colaboradores.
2. `main` protegida; ramas por funcionalidad: `feature/login`, `feature/mood-form`, `feature/agenda`, `feature/help`.
3. Commits pequeños de **ambos**, con mensajes claros (`feat: validación de correo`), y Pull Requests con revisión cruzada.
4. Dividir sugerida: A → login, registro emocional, tema/componentes; B → agenda, ayuda, notificaciones, persistencia.

## Pendientes antes de entregar
- **Logo**: se incluyó el recorte del manual (602 px, `res/drawable-nodpi/logo_vitairos.jpg`). Reemplázalo por el archivo oficial de mejor resolución si lo tienen. No se reescribe ni recolorea.
- **Tipografía Outfit**: agrega los .ttf a `res/font` y cambia `BrandFont` en `ui/theme/Theme.kt` (hoy usa la fuente del sistema).
- **Ícono de la app**: Android Studio > `New > Image Asset` usando el isotipo.
- Verifica los números de ayuda de `HelpScreen.kt` (*4141, 600 360 7777, 131) antes de presentar.
