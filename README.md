# Course KMP

A Kotlin Multiplatform application for Android and iOS with shared Compose Multiplatform UI.

## Project structure

- `androidApp` is the Android application entry point and packages the shared UI as an APK.
- `shared` is a Kotlin Multiplatform library containing shared UI and business logic for Android and iOS.
- `iosApp` is the native Xcode application that embeds the static `Shared` framework.
- `gradle/libs.versions.toml` centralizes the complete Chirp-style dependency and plugin catalog. Catalog entries are available for future features but are only added to module classpaths when used.

This split replaces the older all-in-one `composeApp` layout and keeps the Android application separate from the Android KMP library for AGP 9 compatibility.

## Requirements

- JDK 17
- Android SDK 37
- Android Studio with current Kotlin Multiplatform support
- Xcode on macOS for iOS builds

## Build and test

```shell
./gradlew :androidApp:assembleDebug
./gradlew :shared:testAndroidHostTest
```

On macOS, run the iOS tests with:

```shell
./gradlew :shared:iosSimulatorArm64Test
```

Open `iosApp` in Xcode to run the iOS application. Its build phase invokes `:shared:embedAndSignAppleFrameworkForXcode`.

## Dependency policy

Versions are pinned in the version catalog. Stable releases are preferred, while Compose-owned prerelease coordinates are used only when required by the stable Compose Multiplatform release line. Koin and Ktor bundles mirror the catalog organization used by Chirp.
