# Edición Limitada

**Edición Limitada** es una aplicación móvil desarrollada en **Android Studio con Kotlin y Jetpack Compose (Material 3)**.  
Su propósito es ofrecer una interfaz funcional para la gestión de productos de una tienda de diseño independiente, abordando el proceso de registro, visualización y administración de productos.

Este proyecto corresponde a la **Evaluación Parcial 2** de la asignatura, enfocada en el desarrollo de una interfaz móvil funcional con validaciones, navegación, uso de recursos nativos y almacenamiento local.

---

## Descripción general

Al ingresar a la aplicación, el usuario se encuentra con una pantalla de **inicio de sesión**, que solicita correo y contraseña.  
Tras autenticarse correctamente, se accede a la **pantalla principal de productos**, donde se pueden visualizar los artículos disponibles.  

El menú lateral permite:
- **Listar productos** y editarlos.  
- **Agregar nuevos productos** ingresando su nombre, diseñador, precio y stock.  
- **Capturar una foto** del producto utilizando la **cámara nativa** del dispositivo.  
- **Cerrar sesión** y volver al login.

La aplicación almacena los datos de manera **local**, simulando una base de datos integrada dentro del código, junto con las imágenes en la carpeta `drawable`.

---

## Funcionalidades principales

### Inicio de sesión
- Formulario validado con campos de correo y contraseña.  
- Verificación básica antes de permitir el acceso.  

### Gestión de productos
- Visualización de productos existentes con su información principal.  
- Edición de productos desde el menú lateral.  
- Almacenamiento de imágenes dentro del proyecto.  

### Agregar producto
- Formulario con validaciones en todos los campos.  
- Captura de imagen mediante la cámara nativa.  

### Almacenamiento local
- Persistencia simulada dentro de la aplicación.  
- Sin dependencias externas ni conexión a internet.  

### Navegación
- Implementada con **Navigation Compose**.  
- Menú lateral funcional para cambiar entre secciones.  

### Gestión de estado
- Manejo de datos mediante **ViewModel** y **StateFlow**.  
- Interfaz reactiva ante cambios de estado.  

### Diseño visual
- Interfaz basada en **Material Design 3 (Material You)**.  
- Tipografía, espaciado y color adaptados a la línea visual de Android moderno.  

### Recursos nativos
- Integración de la cámara del dispositivo para tomar fotografías directamente desde la app.  

### Animaciones
- Transiciones suaves y animaciones sutiles en elementos interactivos.

---

## Tecnologías utilizadas

- **Kotlin**
- **Android Studio**
- **Jetpack Compose**
- **Material Design 3**
- **ViewModel + StateFlow**
- **Navigation Compose**

---

## Estructura del proyecto
```plaintext
APPEdicionLimitada_008v_Grupo2/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/vivitasol/carcasamvvm/
│   │   │   │   ├── data/
│   │   │   │   │   └── PrefsDataStore.kt
│   │   │   │   ├── model/
│   │   │   │   │   └── Product.kt 
│   │   │   │   ├── navigation/
│   │   │   │   │   └── AppNav.kt
│   │   │   │   ├── ui/theme/
│   │   │   │   │   ├── Color.kt
│   │   │   │   │   ├── Theme.kt
│   │   │   │   │   └── Type.kt
│   │   │   │   ├── viewmodels/
│   │   │   │   │   ├── CreateProductViewModel.kt
│   │   │   │   │   ├── DetailViewModel.kt
│   │   │   │   │   ├── HomeViewModel.kt
│   │   │   │   │   └── LoginViewModel.kt
│   │   │   │   ├── views/
│   │   │   │   │   ├── CreateProductView.kt
│   │   │   │   │   ├── DetailView.kt
│   │   │   │   │   ├── HomeView.kt
│   │   │   │   │   ├── LoginView.kt
│   │   │   │   │   └── MenuShellView.kt (Menú desplegable)
│   │   │   │   └── MainActivity.kt
│   │   │   ├── res/
│   │   │   │   ├── drawable/   (imágenes de productos y logo)
│   │   │   │   ├── font/ 
│   │   │   │   ├── layout/
│   │   │   │   └── values/
│   │   │   └── AndroidManifest.xml
│   │   └── ...
│   └── build.gradle
│
├── .idea/                (configuración del proyecto)
└── README.md



---

##  Cumplimiento de la rúbrica

| Requisito                      | Estado |
|--------------------------------|:------:|
| Diseño visual con Material 3   | ✅ |
| Formularios validados          | ✅ |
| Navegación funcional           | ✅ |
| Gestión de estado              | ✅ |
| Almacenamiento local           | ✅ |
| Uso de recursos nativos        | ✅ |
| Animaciones                    | ✅ |

---

##  Autoras / Autores

- **Anakena Balbontín**  
- **Betsabé Spring**  

---

##  Nota final

Este proyecto fue desarrollado con fines académicos para la asignatura de desarrollo móvil.  
Integra los principales componentes del ecosistema moderno de Android, combinando **funcionalidad**, **diseño visual** y **uso de recursos nativos**
