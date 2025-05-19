# CoordinApp

> **Prueba Técnica – Desarrollador/a Android (Coordinadora, mayo 2025)**
> Aplicación Android en Java 17 que demuestra autenticación con Firebase, lectura de QR, sincronización offline y despliegue continuo a Firebase App Distribution mediante **GitHub Actions**.

---

## 📲 Funcionalidades clave

| Módulo                 | Descripción                                                                                                                                                                                 |
| ---------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Login → Firestore**  | Valida usuario/contraseña en colección **`Usuarios`**; errores vía Snackbar.                                                                                                                |
| **Escáner QR**         | Cámara (Play Services Code Scanner) captura QR → API REST ([https://noderedtest.coordinadora.com/api/v1/validar](https://noderedtest.coordinadora.com/api/v1/validar)) → valida estructura. |
| **Persistencia local** | Éxitos se guardan en **`backup_local`** (SQLite). Cada 5 lecturas correctas: se sube backup a **colección `backup`** en Firestore.                                                          |
| **RecyclerView**       | Lista en tiempo real de lecturas locales con icono de **Mapa**.                                                                                                                             |
| **Mapa**               | Pantalla horizontal con Google Maps SDK: marcador en lat/long del item + línea desde la ubicación del dispositivo.                                                                          |
| **Logout Seguro**      | Borra documento backup en Firestore y limpia `backup_local`, luego vuelve a Login.                                                                                                          |
| **Entrada manual**     | Campo de texto acepta estructura `etiqueta1d-latitud-longitud-observacion`, la codifica Base64 y recorre el mismo flujo que el QR.                                                          |

---

## 🏗️ Tech Stack & dependencias principales

| Categoría         | Librerías                                                          |
| ----------------- | ------------------------------------------------------------------ |
| **Lenguaje / VM** | **Java 17**, RxJava 3, RxAndroid 3                                 |
| **Arquitectura**  | **MVVM** — ViewModel ➜ Repository ➜ DataSource                     |
| **DI**            | **Dagger 2** (`dagger`, `dagger-android`, kapt)                    |
| **Red**           | **Volley** + OkHttp3 Logging + Gson                                |
| **Firebase**      | BoM (Auth, Firestore, App Distribution)                            |
| **QR / ML**       | Google ML Kit Barcode Scanning, Play Services Code Scanner         |
| **Cámara**        | CameraX View 1.4                                                   |
| **Mapas**         | Google Maps SDK & Play Services Location                           |
| **Tests**         | JUnit4/5, Mockito Core/Inline, Robolectric, Truth, Coroutines Test |

*Consulta todas las versiones exactas en [`app/build.gradle`](./app/build.gradle).*
`compileSdk = 35`, `minSdk = 24`, `targetSdk = 35`, `versionName = "1.0"`

---

## 🧩 Arquitectura & Diseño

### Patrón MVVM (justificación)

* **Presentación desacoplada**: Activity → ViewModel (sin lógica UI).
* **Reactividad**: RxJava 3 maneja flujos asíncronos (API, SQlite, Firestore).
* **Testabilidad**: ViewModels se prueban con JUnit + RxJava TestSchedulers.

### Dagger 2

* Provee ViewModels, Repositories, API Service, Database y Location Provider.
* Alcances: `@Singleton` (app), `@ActivityScoped`, `@ViewModelScoped`.

### Manejo de errores

* Interceptores Rx (`onErrorResumeNext`, `retryWhen`) para red y base local.
* Estados **Loading / Success / Error** unidos a LiveData para feedback UI.

---

## 🔄 CI / CD (GitHub Actions → Firebase)

Archivo: `.github/workflows/android-cicd.yml`

| Job            | Acción                                          | Resultado                                                                                            |
| -------------- | ----------------------------------------------- | ---------------------------------------------------------------------------------------------------- |
| **Test**       | `./gradlew testDebug`                           | Ejecuta pruebas unitarias.                                                                           |
| **Build**      | `./gradlew assembleDebug`                       | Genera `app-debug.apk`.                                                                              |
| **Distribute** | `wzieba/Firebase-Distribution-Github-Action@v1` | Sube APK al proyecto Firebase **CoordinApp** (`elite-ceremony-460219-d6`) ➜ grupo **QA CoordinApp**. |

**Secrets configurados**:

* `FIREBASE_APP_ID` – `1:459598903716:android:9bc77972134a233fdee931`
* `FIREBASE_CREDENTIALS` – JSON de la cuenta de servicio `github-ci@elite-ceremony-460219-d6.iam.gserviceaccount.com`

**Colaboradores del repo**: `ingoskr10`, `oscart@`, `sdhajan@`, `camilov@`.

---

## 🚀 Ejecución local

```bash
# Clonar y abrir en Android Studio Iguana o superior
$ git clone https://github.com/<org>/CoordinApp.git
$ cd CoordinApp

# Compilar y ejecutar unit tests
$ ./gradlew testDebug

# Instalar debug en dispositivo
$ ./gradlew installDebug
```

### Permisos requeridos

* Cámara (QR)
* Ubicación (Maps, línea)

Solicitados en tiempo de ejecución con rationale.

---

## 🧪 Pruebas incluidas

| Ámbito                | Archivo                  | Cobertura                            |
| --------------------- | ------------------------ | ------------------------------------ |
| **Unit**              | `LoginViewModelTest.kt`  | Validación credenciales + flujo Rx.  |
| **Unit**              | `Base64ConverterTest.kt` | Conversión texto → Base64 → texto.   |

---

## 🤝 Contribución

1. Fork → branch feature → PR.
2. Revisión mínima de `ingoskr10` + checks verdes.
3. Merge a `main` ➜ pipeline publica nueva build.

*Convenciones:* ktlint, Java 17, Clean Architecture packages.

---

## 📄 Licencia

Proyecto interno Jonathan Javier Ramirez © 2025. Uso restringido.

---

## 🙏 Agradecimientos

Firebase, Google ML Kit, equipo QA Coordinadora por feedback.

![Android CI/CD](https://github.com/JonathanRamirez1/CoordinApp/actions/workflows/android-cicd.yml/badge.svg)

