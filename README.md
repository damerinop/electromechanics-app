# Electromechanics SpA - Aplicación Android 📱

Sistema móvil nativo para Android orientada a la gestión, registro de jornadas, agenda de servicios y control de acceso del personal técnico de **Electromechanics SpA**.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Kotlin
* **Interfaz de Usuario:** Jetpack Compose (UI Declarativa)
* **Base de Datos Local:** Room Database (SQLite)
* **Patrón de Arquitectura:** MVVM (Model-View-ViewModel)
* **Navegación:** Jetpack Navigation Compose
* **Estilos Visuales:** Material Design 3 (Paleta Industrial personalizada)
* **Control de Versiones:** Git con metodología Git Flow

---

Tablero Trello: [https://trello.com/b/LcYhxT8D/electromechanics-app](https://trello.com/b/LcYhxT8D/electromechanics-app "https://trello.com/b/LcYhxT8D/electromechanics-app")

## 📁 Estructura Completa del Proyecto (`com.electromechanics.app`)

```text
com.electromechanics.app/
├── MainActivity.kt                  # Punto de entrada (Edge-to-edge & statusBarsPadding)
├── data/                            # Capa de Datos (Data Layer)
│   ├── database/
│   │   └── AppDatabase.kt           # Instancia y configuración principal de Room Database
│   └── local/
│       ├── dao/
│       │   ├── EmpleadoDao.kt       # Consultas SQL de Empleados (autenticar, obtenerPorRut, registrarEmpleado)
│       │   └── RegistroJornadaDao.kt # Consultas SQL para registro y control de jornadas
│       └── entity/
│           ├── EmpleadoEntity.kt    # Tabla 'empleados' en la base de datos
│           └── RegistroJornadaEntity.kt # Tabla 'registro_jornadas' en la base de datos
├── ui/                              # Capa de Presentación (UI Layer)
│   ├── components/
│   │   ├── Header.kt                # Encabezado corporativo reutilizable
│   │   └── Footer.kt                # Pie de página institucional reutilizable
│   ├── navigation/
│   │   └── AppNavigation.kt         # Rutas de navegación entre pantallas
│   ├── screen/
│   │   ├── LoginScreen.kt           # Formulario de inicio de sesión
│   │   ├── RegisterScreen.kt        # Formulario de registro de nuevos empleados
│   │   ├── HomeScreen.kt            # Panel principal / Dashboard del empleado
│   │   ├── ControlJornadaScreen.kt  # Marcaje de entrada/salida y estado de turno
│   │   ├── AgendaServiciosScreen.kt # Listado y programación de servicios técnicos
│   │   ├── HistorialJornadasScreen.kt # Registro histórico de turnos trabajados
│   │   ├── SucursalesMapaScreen.kt  # Ubicación de sucursales e instalaciones
│   │   ├── NotificacionesScreen.kt  # Centro de alertas y avisos técnicos
│   │   └── PerfilEmpleadoScreen.kt  # Vista de información personal y credenciales
│   ├── theme/                       # Estilos e Identidad Visual
│   │   ├── Color.kt                 # Paleta Industrial (Black, YellowAccent, ErrorRed)
│   │   └── Theme.kt                 # Configuración del tema general (MaterialTheme)
│   └── viewmodel/
│       └── AuthViewModel.kt         # Lógica de autenticación, validación de RUT único y excepciones
└── utils/                           # Utilidades y Herramientas Auxiliares
    └── RutUtils.kt                  # Algoritmo de validación de dígito verificador y formato de RUT chileno