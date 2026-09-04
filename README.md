# Chirp - Kotlin Multiplatform chat client

Chirp is a mobile messaging client for Android and iOS. It shares the application UI, navigation, business logic, networking, and local database through Kotlin Multiplatform, with thin native entry points for each platform.

The app connects to the hosted Chirp API at `chirp.pl-coding.com`. It demonstrates a production-oriented, modular KMP architecture rather than a basic platform-sharing sample.

## Features

- Account registration, email verification, login, logout, and session restoration
- Forgot-password and deep-linked password-reset flows
- Chat list and message detail views with adaptive list/detail layouts
- One-to-one and group chat creation and participant management
- Real-time messages and chat updates over WebSockets
- Offline-first chat and message storage
- Automatic access-token refresh and connection retry handling
- User profile and profile-picture upload
- Android push-notification token registration with Firebase Cloud Messaging
- Shared Material 3 design system and Compose resources

## Technology stack

| Area | Technology |
| --- | --- |
| Platforms | Android and iOS |
| Language | Kotlin 2.4.10, with a small SwiftUI iOS host |
| UI | Compose Multiplatform 1.11.1, Material 3, Material 3 Adaptive |
| Architecture | Feature modules with presentation, domain, and data layers; ViewModels and unidirectional state/events |
| Navigation | Type-safe Navigation Compose with deep links |
| Dependency injection | Koin 4.2.2 |
| Networking | Ktor 3.5.2, Kotlinx Serialization, HTTP bearer authentication, WebSockets |
| Persistence | Room KMP, bundled SQLite, DataStore |
| Images | Coil 3 |
| Permissions | moko-permissions |
| Logging | Kermit |
| Notifications | Firebase Cloud Messaging on Android |
| Build | Gradle 9.4.1, Android Gradle Plugin 9.2.1, convention plugins, version catalog, BuildKonfig, KSP |

Android uses Ktor's OkHttp engine; iOS uses the Darwin engine. Platform-specific implementations also cover database creation, connectivity and lifecycle observation, URI handling, media picking, and permissions.

## Project structure

```text
Course_KMP/
|-- androidApp/                 Android application and native entry point
|-- iosApp/                     SwiftUI/Xcode host for the shared framework
|-- shared/                     App composition, root navigation, and DI wiring
|-- core/
|   |-- domain/                 Shared models, validation, result/error types, contracts
|   |-- data/                   HTTP client, authentication, session storage, logging
|   |-- presentation/           Shared UI utilities, permissions, adaptive helpers
|   `-- designsystem/           Theme, fonts, icons, and reusable Compose components
|-- feature/
|   |-- auth/
|   |   |-- domain/             Authentication-specific validation and contracts
|   |   `-- presentation/       Login, registration, verification, and password flows
|   `-- chat/
|       |-- domain/             Chat, message, participant, and notification contracts
|       |-- data/               Ktor services, WebSocket client, offline-first repositories
|       |-- database/           Room entities, DAOs, views, and database factories
|       `-- presentation/       Chat list/detail, creation, management, and profile UI
|-- build-logic/                Reusable Gradle convention plugins
`-- gradle/libs.versions.toml   Central dependency and SDK version catalog
```

Most implementation lives in `commonMain`. Android- and iOS-specific source sets provide the native pieces behind shared contracts. The `shared` module produces an Android library consumed by `androidApp` and a static `Shared` framework embedded by `iosApp`.

## Requirements

- Android Studio with Kotlin Multiplatform support
- JDK 21 (the Gradle daemon toolchain is pinned to Azul JDK 21)
- Android SDK 37
- Android 8.0 / API 26 or newer for running the Android app
- macOS with Xcode for building the iOS app
- iOS 18.2 or newer for the current Xcode target
- A Chirp backend API key

## Configuration

Add the backend API key to the root `local.properties` file:

```properties
API_KEY=your_api_key
```

The build generates module-local `BuildKonfig.API_KEY` constants and sends the value as the `X-API-KEY` request header. Keep `local.properties` out of version control.

The backend URLs are currently defined in `core/data/src/commonMain/kotlin/ru/missclick/core/data/networking/UrlConstants.kt`:

```text
https://chirp.pl-coding.com/api
wss://chirp.pl-coding.com/ws
```

### Firebase on Android

Android push-token support uses Firebase Cloud Messaging. The Firebase configuration is not committed to this repository. A complete setup requires a matching `google-services.json` file and the Google Services plugin on the Android application module; in the current checkout, that plugin is applied to `shared` instead, so push-token initialization still needs project-specific configuration.

## Run the app

### Android

Open the repository in Android Studio, select the `androidApp` run configuration, and run it on an API 26+ emulator or device.

Build a debug APK from the command line:

```shell
# macOS/Linux
./gradlew :androidApp:assembleDebug

# Windows
.\gradlew.bat :androidApp:assembleDebug
```

### iOS

iOS builds require macOS. Open `iosApp/iosApp.xcodeproj` in Xcode, choose an iOS simulator or device, set a development team if signing requires it, and run the `iosApp` scheme.

The Xcode build phase invokes:

```shell
./gradlew :shared:embedAndSignAppleFrameworkForXcode
```

This compiles and embeds the shared Kotlin framework automatically.

## Tests

The intended JVM-hosted test command for the shared module is:

```shell
# macOS/Linux
./gradlew :shared:testAndroidHostTest

# Windows
.\gradlew.bat :shared:testAndroidHostTest
```

On an Apple Silicon Mac, run a module's iOS simulator tests with:

```shell
./gradlew :shared:iosSimulatorArm64Test
```

At present, this task does not compile because the enabled `shared` test source sets do not receive the Kotlin test dependency. The other modules contain generated test placeholders, but their Android host/device test source sets are not enabled. Automated feature coverage is therefore not operational yet.

## Architecture notes

- Presentation modules expose Compose screens and ViewModels that consume domain contracts.
- Domain modules contain platform-independent models and interfaces.
- Data modules implement those interfaces with Ktor, DataStore, Room, and platform services.
- Chat repositories use the local Room database as the observable source of truth and refresh it from the API.
- The WebSocket connection reacts to authentication, connectivity, and foreground lifecycle state, then writes incoming updates into the local database.
- Koin modules are assembled in `shared`, which chooses the authentication or chat navigation graph from the restored session.

Authentication verification and password reset support both HTTPS app links and the `chirp://` custom URL scheme.

## Current project status

- `:androidApp:assembleDebug` is verified to complete successfully on Windows.
- `:shared:testAndroidHostTest` currently fails at test compilation because `kotlin.test` is unavailable to the test source sets.
- The iOS host and Kotlin targets are configured, but iOS can only be built on macOS. The current source tree also lacks an iOS implementation and Koin binding for `PushNotificationService`, which must be supplied before treating the iOS app as complete.
- Gradle reports non-blocking configuration warnings for disabled test source sets, the custom `mobileMain` hierarchy, and the Google Services plugin placement.
