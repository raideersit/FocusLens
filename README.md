Entrega 1 Proyecto: Alumno: Alvaro Uirbe

# 🥗 FocusLens

Aplicación multiplataforma (Android & iOS) para escanear alimentos y obtener información nutricional detallada con análisis personalizado según las metas del usuario.

---

## ¿Qué es FocusLens?

FocusLens permite a cualquier persona escanear el código de barras de un alimento y recibir al instante su información nutricional completa, junto con un análisis adaptado al objetivo personal del usuario (perder peso, ganar músculo, alimentación balanceada, etc.).

La idea nació de la necesidad de tomar decisiones alimenticias más informadas de forma rápida y sin tener que buscar manualmente en tablas nutricionales.

---

## Funcionalidades actuales

### 🔐 Autenticación
- Registro e inicio de sesión con correo y contraseña
- Sesión persistente entre reinicios de la app
- Cierre de sesión

### 📷 Escáner de alimentos
- Lectura en tiempo real de códigos de barras (EAN-13, EAN-8, UPC-A, UPC-E)
- Integración nativa por plataforma (CameraX + ML Kit en Android / AVFoundation en iOS)

### 🍎 Detalle nutricional
- Información completa por 100g: calorías, proteínas, carbohidratos, azúcares, grasas, fibra, sodio
- Barras animadas de progreso nutricional
- Indicador NutriScore (A–E)
- Análisis semáforo personalizado según las metas del usuario

### 📋 Historial
- Registro automático de todos los alimentos escaneados
- Eliminar entradas deslizando
- Visualización con fecha y resumen nutricional

### 👤 Perfil
- Nombre y correo del usuario registrado
- Selección de objetivo nutricional
- Configuración de metas diarias (calorías, proteínas, carbohidratos, grasas)
- Guardado con confirmación visual

---

## Stack tecnológico (Kotlin Multiplatform)

La aplicación ha sido reescrita utilizando **Kotlin Multiplatform (KMP)** y **Compose Multiplatform**, permitiendo compartir ~80% del código entre Android y iOS.

| Categoría | Tecnología |
|-----------|-----------|
| Lenguaje | Kotlin |
| UI | Compose Multiplatform |
| Arquitectura | Clean Architecture + MVVM |
| Inyección de dependencias | Koin |
| Base de datos local | SQLDelight |
| Persistencia de sesión | DataStore KMP |
| Red | Ktor |
| API de alimentos | [OpenFoodFacts](https://world.openfoodfacts.org/) |
| Cámara (Android) | CameraX + ML Kit Barcode Scanning |
| Cámara (iOS) | AVFoundation + Vision (Platform expect/actual) |

---

## Estructura del proyecto

El código está dividido en tres módulos principales:

```
FocusLens-AppAndroid-main/
├── shared/                 # Código compartido (KMP)
│   ├── src/commonMain/     # Lógica de negocio, Red, UI (Compose), DI
│   ├── src/androidMain/    # Implementaciones nativas para Android (CameraX)
│   └── src/iosMain/        # Implementaciones nativas para iOS (AVFoundation)
├── app/                    # Aplicación Android (App Host)
│   └── src/main/           # MainActivity y FocusLensApplication
└── iosApp/                 # Aplicación iOS (App Host)
    └── iosApp/             # iOSApp.swift y configuración de Xcode
```

---

## API utilizada

**OpenFoodFacts** — Base de datos abierta y colaborativa de más de 3 millones de productos alimenticios de todo el mundo.

- Endpoint: `https://world.openfoodfacts.org/api/v0/product/{barcode}.json`
- Gratuita, sin necesidad de API key
- Datos verificados por la comunidad

---

## Equipo

Proyecto universitario desarrollado por 2 personas como parte del curso de Desarrollo de Aplicaciones Móviles.

---

## Licencia

Uso académico — Universidad de Los Lagos
