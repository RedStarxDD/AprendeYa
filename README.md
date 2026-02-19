# AprendeYa 📚

Una aplicación Android desarrollada en Kotlin para facilitar el aprendizaje y la educación.

## 📋 Descripción

AprendeYa es una plataforma educativa móvil diseñada para proporcionar una experiencia de aprendizaje interactiva y accesible. Esta aplicación permite a los usuarios acceder a contenido educativo de calidad desde sus dispositivos Android.

## ✨ Características

- 📱 Interfaz amigable e intuitiva
- 🎓 Contenido educativo organizado y estructurado
- 🔄 Sincronización de datos en tiempo real
- 🔐 Autenticación segura de usuarios

## 🛠️ Stack Tecnológico

### Lenguaje y Framework
- **Kotlin** 1.9+ - Lenguaje principal con toda su potencia expresiva
- **Android SDK** - API 21 (Lollipop) como mínimo, API 34 como objetivo
- **Jetpack Compose** - UI declarativa moderna

### Arquitectura y Patrones
- **MVVM** (Model-View-ViewModel) - Patrón de arquitectura principal
- **Repository Pattern** - Abstracción de fuentes de datos
- **Dependency Injection** - Inyección de dependencias con Hilt

### Librerías Principales
- **Material Design 3** - Componentes de interfaz moderna
- **Navigation 3** - Navegación eficiente entre pantallas
- **Firebase Authentication** - Autenticación segura
- **Firestore** - Base de datos NoSQL en la nube
- **StateFlow** - Estado reactivo moderno
- **Hilt** - Inyección de dependencias construida sobre Dagger

### Flujo de Datos

```
UI (Screens/Composables)
    ↓
ViewModel (StateFlow)
    ↓
Repository (Abstracción de datos)
    ↓
Database (Nube)
```
## 🎯 Roadmap

Características planeadas para futuras versiones:
- [ ] Cuestionarios interactivos
- [ ] Análisis de progreso
- [ ] Sincronización offline
- [ ] Cuenta para profesores
- [ ] Creación y asignación de cursos desde la app

---

⭐ Si te gusta este proyecto, ¡no olvides darle una estrella!
