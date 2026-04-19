# Formulario - Jetpack Compose

Esta es una aplicación de Android construida con **Jetpack Compose** que implementa un formulario de perfil de usuario completo, con validaciones en tiempo real y navegación entre pantallas.

## 🚀 Características

- **Validaciones dinámicas:** Control de errores en nombre, apellido, fecha de nacimiento, genero, número de telefono, correo electrónico.
- **Selección de Fecha:** Implementación de `DatePickerDialog` con validación de edad mínima (13 años).
- **Selector de País:** Integración de la librería `KomposeCountryCodePicker` para números telefónicos internacionales.
- **Diseño Moderno:** Uso de `Material3`, fuentes personalizadas (Plus Jakarta Sans, Manrope) y componentes visuales como `OutlinedTextField` con bordes redondeados.
- **Navegación segura:** Uso de `Type-safe Navigation` de Compose para pasar datos entre la pantalla del formulario y la pantalla de resumen, tambien utilizando la serializacion para poder pasar los datos entre pantallas.
- **Modo adaptativo:** Soporte para scroll vertical en pantallas pequeñas.

## 🛠️ Tecnologías utilizadas

- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Navegación:** Compose Navigation (Safe Args)
- **Librerías externas:**
    - `KomposeCountryCodePicker` (para ladas internacionales).
    - `Kotlinx Serialization` (para el paso de datos en navegación).

## 📋 Requisitos

- Android Studio.
- Dispositivo o emulador con Android 8.0 (API 26) o superior.

## ⚙️ Instalación

1. Clona el repositorio:
    
    ```bash
    git clone [https://github.com/insannity/formulario-app.git](https://github.com/rgonzalezarre/FormularioJC.git)
    ```
