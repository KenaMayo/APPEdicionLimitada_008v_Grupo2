

# 📱 Edición Limitada — Aplicación Móvil + Backend Spring Boot

Edición Limitada es una solución completa compuesta por:

* **Aplicación móvil Android** desarrollada en *Kotlin + Jetpack Compose*
* **Backend REST** desarrollado con *Spring Boot 3.5 + MongoDB*
* **Despliegue en Railway + MongoDB Atlas**

Su propósito es gestionar clientes, productos, autenticación y funcionalidades de compra para una tienda de diseño independiente.

---

# 🧩 Índice

1. [Aplicación Móvil (Kotlin + Jetpack Compose)](#-aplicación-móvil-kotlin--jetpack-compose)
2. [Características Principales](#-características-principales)
3. [Estructura del Proyecto Android](#-estructura-del-proyecto-android)
4. [Dependencias Android](#-dependencias-principales-gradle)
5. [Permisos Android](#-androidmanifest--permisos-principales)
6. [Flujo del Usuario](#-flujo-de-usuario)
7. [Cumplimiento de Requerimientos](#-cumplimiento-de-requerimientos)
8. [Backend (Spring Boot 3.5 + MongoDB)](#️-edición-limitada--backend-spring-boot-35--mongodb)
9. [Estructura del Proyecto Backend](#-estructura-del-proyecto-backend)
10. [Dependencias del Backend (POM.xml)](#-dependencias-utilizadas-pomxml)
11. [Seguridad y Endpoints](#-seguridad-spring-security)
12. [Despliegue Completo: App → Backend → MongoDB](#-despliegue-de-la-aplicación-edición-limitada)
13. [Requisitos para Ejecutar Localmente](#-requisitos-para-ejecutar-la-app-localmente)
14. [APK Firmada](#-apk-firmada-entrega-final)
15. [Tecnologías Utilizadas](#-tecnologías-utilizadas)

---

# 📱 Aplicación Móvil (Kotlin + Jetpack Compose)

Edición Limitada es una app Android creada con:

* **Kotlin**
* **Jetpack Compose**
* **MVVM**
* **Navigation Compose**
* **Room + DataStore**
* **Retrofit + Gson**

Conexión directa a un backend en Railway mediante HTTPS.

---

# 🚀 Características Principales

## 🔐 Autenticación

* Registro e inicio de sesión
* Manejo de sesión persistente (DataStore)
* Validaciones de correo, contraseña y duplicados

## 👤 Gestión de usuarios

* Perfil de usuario conectado
* Edición de datos personales
* Vistas: *UserHome*, *UserProfile*

## 🛍 Gestión de productos

* Listado desde backend
* CRUD completo
* Subida de imágenes (cámara/galería)
* Vista de detalle

## 🧾 Gestión de clientes (Admin)

* Listado completo
* Crear, editar, eliminar
* Búsqueda por email

## 🛒 Carrito

* Agregar productos
* Modificar cantidades
* Vista modal con resumen

## 📸 Cámara & Galería

* FileProvider
* Permisos para API 31+
* Selección desde galería

## 📡 Conexión API REST

* Retrofit + Gson
* NullOnEmptyConverterFactory
* Logging interceptor

## 🗄 Almacenamiento Local

* **Room** para productos/carrito
* **DataStore** para token y correo

## 🧭 Navegación

* Navigation Compose
* Rutas separadas por rol (Admin / User)
* SplashScreen integrada

## 🎨 Diseño Visual

* Material Design 3
* Tema y colores personalizados
* Animaciones y transiciones suaves

---

# 📂 Estructura del Proyecto Android

```plaintext
app/
└── src/main/java/com/vivitasol/carcasamvvm/
    ├── data/
    │   ├── AppDatabase
    │   ├── ClientRepository
    │   ├── ProductRepository
    │   └── PrefsDataStore.kt
    │
    ├── model/
    │   ├── Cliente
    │   ├── ClienteRequest
    │   ├── Product
    │   └── CartItem
    │
    ├── remote/
    │   ├── ApiClient
    │   ├── ClienteService
    │   ├── ProductoService
    │   └── NullOnEmptyConverterFactory
    │
    ├── navigation/
    │   └── AppNav.kt
    │
    ├── viewmodels/
    │   ├── LoginViewModel
    │   ├── RegisterViewModel
    │   ├── HomeViewModel
    │   ├── UserHomeViewModel
    │   ├── EditProfileViewModel
    │   ├── CreateProductViewModel
    │   ├── DetailViewModel
    │   ├── ClienteViewModel
    │   ├── UserProfileViewModel
    │   ├── CartViewModel
    │   └── ViewModelFactory
    │
    ├── views/
    │   ├── LoginView.kt
    │   ├── RegisterView.kt
    │   ├── HomeView.kt
    │   ├── UserHomeView.kt
    │   ├── UserProfileView.kt
    │   ├── EditProfileView.kt
    │   ├── CreateProductView.kt
    │   ├── ClienteListView.kt
    │   ├── ClienteDetailView.kt
    │   ├── UserCartView.kt
    │   ├── MenuShellView.kt
    │   └── UserMenuShellView.kt
    │
    ├── LimitedEditionApp.kt
    └── MainActivity.kt
```

---

# 🛠 Dependencias Principales (Gradle)

## 📡 Retrofit + Gson

```gradle
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
```

## 🗄 Room

```gradle
implementation("androidx.room:room-runtime:2.8.4")
ksp("androidx.room:room-compiler:2.8.4")
```

## 🔐 DataStore

```gradle
implementation("androidx.datastore:datastore-preferences:1.1.1")
```

## 📷 Cámara + Galería

```gradle
implementation("io.coil-kt:coil-compose:2.7.0")
implementation("com.google.accompanist:accompanist-permissions:0.32.0")
```

---

# 📜 AndroidManifest — Permisos principales

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />
```

---

# ▶ Flujo de Usuario

1. **Splash Screen**
   Carga + validación de sesión

2. **Login / Registro**
   Token + email → DataStore

3. **Menú principal**

   * Usuario: productos, carrito, perfil
   * Admin: CRUD clientes/productos

4. **Cámara / Galería**
   Subida de imágenes para productos

5. **Persistencia**

   * Sesión → DataStore
   * Productos offline → Room

---

# 🧪 Cumplimiento de Requerimientos

| Requisito             | Estado |
| --------------------- | ------ |
| Pantallas funcionales | ✅      |
| Autenticación         | ✅      |
| Navegación            | ✅      |
| Cámara y galería      | ✅      |
| Retrofit + API real   | ✅      |
| Room + DataStore      | ✅      |
| Validaciones          | ✅      |
| Material 3            | ✅      |
| Arquitectura MVVM     | ✅      |

---

# 🛠️ Edición Limitada — Backend (Spring Boot 3.5 + MongoDB)

El backend está construido con:

* Java 17
* Spring Boot 3.5.6
* Spring Web
* Spring Security
* MongoDB
* Lombok
* Thymeleaf (para pruebas)

---

# 📦 Estructura del Proyecto Backend

```plaintext
src/main/java/com/example/edicionlimitada/back_end/
│
├── controller/
│   ├── AuthController.java
│   └── ClienteController.java
│
├── model/
│   └── Cliente.java
│
├── repository/
│   └── ClienteRepository.java
│
├── securityconfig/
│   ├── SecurityConfig.java
│   └── WebConfig.java
│
├── service/
│   ├── AuthService.java
│   ├── ClienteService.java
│   └── BackEndApplication.java
│
└── resources/
    ├── application.properties
    ├── static/
    └── templates/
```

---

# 🍃 Base de datos MongoDB — Estructura JSON

```json
{
  "_id": "string",
  "nombre": "string",
  "email": "string",
  "contrasena": "string (hashed)",
  "comuna": "string",
  "region": "string",
  "_class": "com.example.edicionlimitada.back_end.model.Cliente"
}
```

---

# 📚 Dependencias utilizadas (POM.XML)

```xml
<dependencies>
    <!-- MongoDB -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>

    <!-- Thymeleaf -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>

    <!-- Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Seguridad -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- DevTools -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

# 🔐 Seguridad (Spring Security)

✔ BCrypt
✔ Filtros de autenticación
✔ Protección de rutas
✔ CORS habilitado

---

# 🔗 Endpoints principales

## AuthController

| Método | Endpoint         | Acción   |
| ------ | ---------------- | -------- |
| POST   | `/auth/login`    | Login    |
| POST   | `/auth/register` | Registro |
| GET    | `/auth/test`     | Prueba   |

## ClienteController

| Método | Endpoint         | Acción     |
| ------ | ---------------- | ---------- |
| GET    | `/clientes`      | Listar     |
| GET    | `/clientes/{id}` | Obtener    |
| POST   | `/clientes`      | Crear      |
| PUT    | `/clientes/{id}` | Actualizar |
| DELETE | `/clientes/{id}` | Eliminar   |

---

# 📘 Despliegue de la Aplicación Edición Limitada

## 🚀 Arquitectura General

```
Android App (Kotlin)
        |
        v
Spring Boot API (Railway)
        |
        v
MongoDB Atlas
```

### ✔ Backend en Railway

* Detecta Spring Boot automáticamente
* Comando de build:

  ```
  ./mvnw clean package -DskipTests
  java -jar target/back-end-0.0.1-SNAPSHOT.jar
  ```
* URL pública:

  ```
  https://appedicionlimitada008vgrupo2-production.up.railway.app/
  ```

### ✔ Conexión MongoDB Atlas

```
mongodb+srv://edicionlimitada:<password>@clouster1...
```

### ✔ App Android → Retrofit

```kotlin
private const val BASE_URL =
    "https://appedicionlimitada008vgrupo2-production.up.railway.app/"
```

---

# 6️⃣ Requisitos para ejecutar la app localmente

### Backend

* Java 17
* Maven 3.8+
* Spring Boot 3.5+

### Android

* Android Studio Hedgehog+
* Min SDK 24

### Variables importantes

| Entorno     | Variable                  |
| ----------- | ------------------------- |
| Railway     | `PORT`                    |
| Spring Boot | `spring.data.mongodb.uri` |
| Android     | `BASE_URL`                |

---

# 📦 APK Firmada (Entrega Final)

Incluye:

✔ Conexión al backend
✔ CRUD de usuarios
✔ Validación de login
✔ Funcionalidad admin
✔ Conexión estable a MongoDB Atlas

---

# 🔧 Tecnologías Utilizadas

* Kotlin
* Android Studio
* Jetpack Compose
* Retrofit
* Spring Boot
* MongoDB Atlas
* Railway
* Maven
* Java 17

---

👥 Autoras 
Anakena Balbontín 
Betsabé Spring
